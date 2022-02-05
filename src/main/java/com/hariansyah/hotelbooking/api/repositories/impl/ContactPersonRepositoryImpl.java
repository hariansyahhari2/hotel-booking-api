package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.entities.Company;
import com.hariansyah.hotelbooking.api.entities.ContactPerson;
import com.hariansyah.hotelbooking.api.entities.Region;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.ContactPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContactPersonRepositoryImpl implements ContactPersonRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT " +
                "cp.id, cp.first_name, cp.last_name, cp.position, cp.contact_number, " +
                "c.id as company_id, c.name as c_name, " +
                "ct.id as city_id, ct.name as ct_name, " +
                "r.id as region_id, r.name as r_name " +
                "FROM contact_person cp " +
                "LEFT JOIN company c " +
                "ON cp.company_id = c.id " +
                "LEFT JOIN city ct " +
                "ON c.city_id = ct.id " +
                "LEFT JOIN region r " +
                "ON ct.region_id = r.id ";
    }

    private ContactPerson entityMapper(ResultSet rs) throws SQLException {
        ContactPerson entity = new ContactPerson();
        Company company = new Company();
        City city = new City();
        Region region = new Region();

        region.setId(rs.getInt("region_id"));
        region.setName(rs.getString("r_name"));

        city.setId(rs.getInt("city_id"));
        city.setName(rs.getString("ct_name"));
        city.setRegion(region);
        company.setCity(city);

        company.setId(rs.getInt("company_id"));
        company.setName(rs.getString("c_name"));

        entity.setId(rs.getInt("id"));
        entity.setFirstName(rs.getString("first_name"));
        entity.setLastName(rs.getString("last_name"));
        entity.setPosition(rs.getString("position"));
        entity.setContactNumber(rs.getString("contact_number"));
        entity.setCompany(company);
        return entity;
    }

    @Override
    public List<ContactPerson> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE cp.is_deleted = 0").toString();
        List<ContactPerson> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public ContactPerson findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE cp.id = ? AND cp.is_deleted = 0").toString();
        List<ContactPerson> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if(entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save(ContactPerson entity) {
        String query = "INSERT INTO contact_person" +
                "(created_date, is_deleted, first_name, last_name, position, contact_number, company_id) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getFirstName(), entity.getLastName(), entity.getPosition(),
                entity.getContactNumber(), entity.getCompany().getId());
        return true;
    }

    @Override
    public Boolean edit(ContactPerson entity) {
        String query = "UPDATE contact_person " +
                "SET modified_date=?, first_name=?, last_name=?, position=?, contact_number=?, company_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getFirstName(), entity.getLastName(), entity.getPosition(),
                entity.getContactNumber(), entity.getCompany().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE contact_person " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
