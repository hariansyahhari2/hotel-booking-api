package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Company;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT " +
                "c.id, c.name, " +
                "r.id as city_id, ct.name as ct_name, " +
                "r.id as region_id, r.name as r_name " +
                "FROM company c LEFT JOIN city ct " +
                "ON c.city_id = ct.id " +
                "LEFT JOIN region r " +
                "ON ct.region_id = r.id ";
    }

    private Company entityMapper(ResultSet rs) throws SQLException {
        Company entity = new Company();
        City city = new City();
        Region region = new Region();

        region.setId(rs.getInt("region_id"));
        region.setName(rs.getString("r_name"));

        city.setId(rs.getInt("city_id"));
        city.setName(rs.getString("name"));
        city.setRegion(region);
        entity.setCity(city);

        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        return entity;
    }

    @Override
    public List<Company> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE c.is_deleted = 0").toString();
        List<Company> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public Company findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE c.id = ? AND c.is_deleted = 0").toString();
        List<Company> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if(entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    public Boolean save(Company entity) {
        String query = "INSERT INTO company(created_date, is_deleted, name, city_id) values (?,?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getName(), entity.getCity().getId());
        return true;
    }

    public Boolean edit(Company entity) {
        String query = "UPDATE company " +
                "SET modified_date=?, name=?, city_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getName(), entity.getCity().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE company " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
