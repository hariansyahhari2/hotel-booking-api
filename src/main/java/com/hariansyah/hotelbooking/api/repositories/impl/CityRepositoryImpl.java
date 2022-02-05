package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT c.id, c.name, " +
                "r.id as region_id, r.name as r_name " +
                "FROM city c LEFT JOIN region r " +
                "ON c.region_id = r.id ";
    }

    private City entityMapper(ResultSet rs) throws SQLException {
        City entity = new City();
        Region region = new Region();

        region.setId(rs.getInt("region_id"));
        region.setName(rs.getString("r_name"));

        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        entity.setRegion(region);
        return entity;
    }

    @Override
    public List<City> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE c.is_deleted = 0").toString();
        List<City> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public City findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE c.id=? AND c.is_deleted = 0").toString();
        List<City> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if (entityList.size() == 0 ) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save(City entity) {
        String query = "INSERT INTO city(created_date, is_deleted, name, region_id) values (?, ?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getName(), entity.getRegion().getId());
        return true;
    }

    public Boolean edit(City entity) {
        String query = "UPDATE city " +
                "SET modified_date=?, name=?, region_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getName(), entity.getRegion().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE city " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
