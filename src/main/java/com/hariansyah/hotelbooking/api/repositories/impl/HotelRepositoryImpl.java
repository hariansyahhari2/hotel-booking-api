package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Company;
import com.hariansyah.hotelbooking.api.entities.Hotel;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT h.id, h.name, h.about, " +
                "c.id as company_id, c.name as c_name, " +
                "ct.id as city_id, ct.name as ct_name, " +
                "r.id as region_id, r.name as r_name, " +
                "ct2.id as ct2_id, ct2.name as ct2_name, " +
                "r2.id as r2_id, r2.name as r2_name " +
                "FROM hotel h LEFT JOIN company c ON h.company_id = c.id " +
                "LEFT JOIN city ct ON c.city_id = ct.id " +
                "LEFT JOIN region r ON ct.region_id = r.id " +
                "LEFT JOIN city ct2 ON h.city_id = ct2.id " +
                "LEFT JOIN region r2 ON ct.region_id = r2.id ";
    }

    private Hotel entityMapper(ResultSet rs) throws SQLException {
        Hotel entity = new Hotel();
        Company company = new Company();
        City city = new City();
        Region region = new Region();
        City ct2 = new City();
        Region r2 = new Region();

        region.setId(rs.getInt("region_id"));
        region.setName(rs.getString("r_name"));

        city.setId(rs.getInt("city_id"));
        city.setName(rs.getString("ct_name"));
        city.setRegion(region);
        company.setCity(city);

        company.setId(rs.getInt("company_id"));
        company.setName(rs.getString("c_name"));

        r2.setId(rs.getInt("r2_id"));
        r2.setName(rs.getString("r2_name"));

        ct2.setId(rs.getInt("ct2_id"));
        ct2.setName(rs.getString("ct2_name"));
        ct2.setRegion(region);

        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        entity.setAbout(rs.getString("about"));
        entity.setCompany(company);
        entity.setCity(ct2);

        return entity;
    }

    @Override
    public List<Hotel> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE h.is_deleted = 0").toString();
        List<Hotel> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public Hotel findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE h.id = ? AND h.is_deleted = 0").toString();
        List<Hotel> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if(entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save(Hotel entity) {
        String query = "INSERT INTO hotel" +
                "(created_date, is_deleted, name, about, company_id, city_id) values (?,?,?,?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getName(), entity.getAbout(),
                entity.getCompany().getId(), entity.getCity().getId());
        return true;
    }

    @Override
    public Boolean edit(Hotel entity) {
        String query = "UPDATE hotel " +
                "SET modified_date=?, name=?, about=?, company_id=?, city_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getName(), entity.getAbout(),
                entity.getCompany().getId(), entity.getCity().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE hotel " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
