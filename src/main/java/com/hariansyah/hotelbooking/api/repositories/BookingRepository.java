package com.hariansyah.hotelbooking.api.repositories;

import com.hariansyah.hotelbooking.api.entities.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends CommonJDBCRepository<Booking, Integer> {
    Long findNumberOfBooked(LocalDate checkInRequest, LocalDate checkOutRequest, Integer roomId);

    List<Booking> findAllBookingByHotelWithTimeRange(LocalDate firstTimeRequest, LocalDate secondTimeRequest, Integer hotelId);

    List<Booking> findAllBookingByRoomWithTimeRange(LocalDate firstTimeRange, LocalDate secondTimeRange, Integer roomId);

    List<Booking> findAllBookingbyRoomAllTime(Integer roomId);

    List<Booking> findAllBookingByHotelAllTime(Integer hotelId);
}
