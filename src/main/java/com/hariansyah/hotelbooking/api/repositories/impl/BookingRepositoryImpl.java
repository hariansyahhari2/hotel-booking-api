package com.hariansyah.hotelbooking.api.repositories.impl;

import com.hariansyah.hotelbooking.api.entities.*;
import com.hariansyah.hotelbooking.api.enums.IdentityCategoryEnum;
import com.hariansyah.hotelbooking.api.enums.RoleEnum;
import com.hariansyah.hotelbooking.api.enums.StatusEnum;
import com.hariansyah.hotelbooking.api.exceptions.EntityNotFoundException;
import com.hariansyah.hotelbooking.api.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String selectQuery() {
        return "SELECT b.id, b.check_in_date, b.check_out_date, b.person_count, b.number_of_night, b.sub_total, b.status, b.room_count, " +
                "rm.id as room_id, rm.room_type as rm_room_type, rm.about as rm_about, rm.price as rm_price, rm.number_of_room as rm_number_of_room, " +
                "h.id as hotel_id, h.name as h_name, h.about as h_about, " +
                "c.id as company_id, c.name as c_name, " +
                "ct.id as city_id, ct.name as ct_name, " +
                "r.id as region_id, r.name as r_name, " +
                "ct2.id as ct2_id, ct2.name as ct2_name, " +
                "r2.id as r2_id, r2.name as r2_name," +
                "g.id as guest_id, g.identity_category as g_identity_category, g.identification_number as g_identification_number, g.first_name as g_first_name, g.last_name as g_last_name, g.address as g_address, " +
                "ac.id as account_id, ac.username as ac_username, ac.email as ac_email, ac.role as ac_role," +
                "bb.id as booked_by_id, bb.username as bb_username, bb.email as bb_email, bb.role as bb_role " +
                "FROM booking b LEFT JOIN room rm ON b.room_id=rm.id " +
                "LEFT JOIN hotel h ON rm.hotel_id=h.id " +
                "LEFT JOIN company c ON h.company_id = c.id " +
                "LEFT JOIN city ct ON c.city_id = ct.id " +
                "LEFT JOIN region r ON ct.region_id = r.id " +
                "LEFT JOIN city ct2 ON h.city_id = ct2.id " +
                "LEFT JOIN region r2 ON ct.region_id = r2.id " +
                "LEFT JOIN customer_identity g ON b.guest=g.id " +
                "LEFT JOIN account ac ON g.account_id=ac.id " +
                "LEFT JOIN account bb ON b.booked_by=bb.id ";
    }

    private Booking entityMapper(ResultSet rs) throws SQLException {
        Booking entity = new Booking();
        Room room = new Room();
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

        room.setId(rs.getInt("room_id"));
        room.setRoomType(rs.getString("rm_room_type"));
        room.setAbout(rs.getString("rm_about"));
        room.setPrice(rs.getDouble("rm_price"));
        room.setNumberOfRoom(rs.getInt("rm_number_of_room"));
        room.setHotel(hotel);

        CustomerIdentity guest = new CustomerIdentity();
        Account account = new Account();

        account.setId(rs.getInt("account_id"));
        account.setUsername(rs.getString("ac_username"));
        account.setEmail(rs.getString("ac_email"));
        account.setRole(RoleEnum.values()[rs.getInt("ac_role")]);

        guest.setId(rs.getInt("guest_id"));
        guest.setIdentityCategory(IdentityCategoryEnum.values()[rs.getInt("g_identity_category")]);
        guest.setIdentificationNumber(rs.getString("g_identification_number"));
        guest.setFirstName(rs.getString("g_first_name"));
        guest.setLastName(rs.getString("g_last_name"));
        guest.setAddress(rs.getString("g_address"));
        guest.setAccount(account);

        Account bookedBy = new Account();
        bookedBy.setId(rs.getInt("booked_by_id"));
        bookedBy.setUsername(rs.getString("bb_username"));
        bookedBy.setEmail(rs.getString("bb_email"));
        bookedBy.setRole(RoleEnum.values()[rs.getInt("bb_role")]);

        entity.setId(rs.getInt("id"));
        entity.setCheckInDate(LocalDate.parse(rs.getString("check_in_date")));
        entity.setCheckOutDate(LocalDate.parse(rs.getString("check_out_date")));
        entity.setPersonCount(rs.getInt("person_count"));
        entity.setNumberOfNight(rs.getLong("number_of_night"));
        entity.setSubTotal(rs.getDouble("sub_total"));
        entity.setStatus(StatusEnum.values()[rs.getInt("status")]);
        entity.setRoomCount(rs.getLong("room_count"));
        entity.setGuest(guest);
        entity.setRoom(room);
        entity.setBookedBy(bookedBy);

        return entity;
    }

    @Override
    public List<Booking> findAll() {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE b.is_deleted = 0").toString();
        List<Booking> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs));
        return entityList;
    }

    @Override
    public Booking findById(Integer id) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE b.id = ? AND b.is_deleted = 0").toString();
        List<Booking> entityList = jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), id);
        if(entityList.size() == 0) throw new EntityNotFoundException();
        return entityList.get(0);
    }

    @Override
    public Boolean save(Booking entity) {
        String query = "INSERT INTO booking" +
                "(created_date, is_deleted, check_in_date, check_out_date, number_of_night, person_count, room_count, status, sub_total, booked_by, guest, room_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(query, LocalDateTime.now(), Boolean.FALSE, entity.getCheckInDate(), entity.getCheckOutDate(), entity.getNumberOfNight(), entity.getPersonCount(),
                entity.getRoomCount(), entity.getStatus().ordinal(), entity.getSubTotal(), entity.getBookedBy().getId(), entity.getGuest().getId(), entity.getRoom().getId());
        return true;
    }

    @Override
    public Boolean edit(Booking entity) {
        String query = "UPDATE booking " +
                "SET modified_date=?, check_in_date=?, check_out_date=?, number_of_night=?, person_count=?, room_count=?, status=?, sub_total=?, booked_by=?, guest=?, room_id=? " +
                "WHERE id=? AND is_deleted = 0";
        jdbcTemplate.update(query, LocalDateTime.now(), entity.getCheckInDate(), entity.getCheckOutDate(), entity.getNumberOfNight(), entity.getPersonCount(),
                entity.getRoomCount(), entity.getStatus().ordinal(), entity.getSubTotal(), entity.getBookedBy().getId(), entity.getGuest().getId(), entity.getRoom().getId(), entity.getId());
        return true;
    }

    @Override
    public Boolean remove (Integer id) {
        String query = "UPDATE booking " +
                "SET is_deleted=?, modified_date=? " +
                "WHERE id=?";
        jdbcTemplate.update(query, Boolean.TRUE, LocalDateTime.now(), id);
        return true;
    }

    @Override
    public Long findNumberOfBooked(LocalDate checkInRequest, LocalDate checkOutRequest, Integer roomId) {
        String query = "SELECT b.id, b.room_count FROM booking b " +
                "WHERE b.check_in_date <= ? " +
                "AND b.check_out_date >= ? " +
                "AND b.status != 1 " +
                "AND b.room_id = ? " +
                "AND b.is_deleted = 0";
        List<Booking> entityList = jdbcTemplate.query(query, (rs, i) -> {
            Booking entity = new Booking();
            entity.setId(rs.getInt("id"));
            entity.setRoomCount(rs.getLong("room_count"));

            return entity;
        }, checkOutRequest, checkInRequest, roomId);


        Long numberOfBookedRoom = 0L;

        for (Booking checkedRoom : entityList) {
            numberOfBookedRoom += checkedRoom.getRoomCount();
        }

        return numberOfBookedRoom;
    }

    @Override
    public List<Booking> findAllBookingByHotelWithTimeRange(LocalDate firstTimeRequest, LocalDate secondTimeRequest, Integer hotelId) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE b.check_in_date <= ? " +
                "AND b.check_out_date >= ? " +
                "AND h.id = ? " +
                "AND b.is_deleted = 0").toString();
        return jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), secondTimeRequest, firstTimeRequest, hotelId);
    }

    @Override
    public List<Booking> findAllBookingByRoomWithTimeRange(LocalDate firstTimeRange, LocalDate secondTimeRange, Integer roomId) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE b.check_in_date <= ? " +
                "AND b.check_out_date >= ? " +
                "AND b.room_id = ? " +
                "AND b.is_deleted = 0").toString();

        return jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), secondTimeRange, firstTimeRange, roomId);
    }

    @Override
    public List<Booking> findAllBookingbyRoomAllTime(Integer roomId) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE r.id=? AND b.is_deleted = 0").toString();

        return jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), roomId);
    }

    @Override
    public List<Booking> findAllBookingByHotelAllTime(Integer hotelId) {
        StringBuilder builder = new StringBuilder();
        String query = builder.append(selectQuery()).append("WHERE h.id=? AND b.is_deleted = 0").toString();

        return jdbcTemplate.query(query, (rs, i) -> entityMapper(rs), hotelId);
    }
}
