package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.*;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT rm.id, rm.room_type, rm.about, rm.price, rm.number_of_room, " +
                "h.id as hotel_id, h.name as h_name, h.about as h_about, " +
                "c.id as company_id, c.name as c_name, " +
                "ct.id as city_id, ct.name as ct_name, " +
                "r.id as region_id, r.name as r_name, " +
                "ct2.id as ct2_id, ct2.name as ct2_name, " +
                "r2.id as r2_id, r2.name as r2_name " +
                "FROM room rm LEFT JOIN hotel h ON rm.hotel_id=h.id " +
                "LEFT JOIN company c ON h.company_id = c.id " +
                "LEFT JOIN city ct ON c.city_id = ct.id " +
                "LEFT JOIN region r ON ct.region_id = r.id " +
                "LEFT JOIN city ct2 ON h.city_id = ct2.id " +
                "LEFT JOIN region r2 ON ct.region_id = r2.id ";
    }

    private Room entityMapper(ResultSet rs) throws SQLException {
        Room entity = new Room();
        Hotel hotel = new Hotel();
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

        hotel.setId(rs.getInt("hotel_id"));
        hotel.setName(rs.getString("h_name"));
        hotel.setAbout(rs.getString("h_about"));
        hotel.setCompany(company);
        hotel.setCity(ct2);

        entity.setId(rs.getInt("id"));
        entity.setRoomType(rs.getString("room_type"));
        entity.setAbout(rs.getString("about"));
        entity.setPrice(rs.getDouble("price"));
        entity.setNumberOfRoom(rs.getInt("number_of_room"));
        entity.setHotel(hotel);

        return entity;
    }

    @Override
    public List<Room> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE rm.is_deleted = 0").toString();
        List<Room> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public Room findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE rm.id = ? AND rm.is_deleted = 0").toString();
        List<Room> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if(entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save(Room entity) {
        String query = "INSERT INTO room" +
                "(created_date, is_deleted, room_type, about, price, number_of_room, hotel_id) values (?,?,?,?,?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getRoomType(), entity.getAbout(),
                entity.getPrice(), entity.getNumberOfRoom(), entity.getHotel().getId());
        return true;
    }

    @Override
    public Boolean edit(Room entity) {
        String query = "UPDATE room " +
                "SET modified_date=?, room_type=?, about, price=?, number_of_room=?, hotel_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getRoomType(), entity.getAbout(),
                entity.getPrice(), entity.getNumberOfRoom(), entity.getHotel().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE room " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }
}
