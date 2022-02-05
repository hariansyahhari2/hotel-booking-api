package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RegionRepositoryImpl implements RegionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT * FROM region ";
    };

    private Region entityMapper(ResultSet rs) throws SQLException {
        Region entity = new Region();
        entity.setId(rs.getInt("id"));
        entity.setName(rs.getString("name"));
        return entity;
    }

    @Override
    public List<Region> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE is_deleted = 0").toString();
        List<Region> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public Region findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE id=? AND is_deleted = 0").toString();
        List<Region> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if (entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save (Region entity) {
        String query = "INSERT INTO region(created_date, is_deleted, name) values (?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getName());
        return true;
    }

    @Override
    public Boolean edit(Region entity) {
        String query = "UPDATE region " +
                "SET modified_date=?, name=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getName(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE region " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
