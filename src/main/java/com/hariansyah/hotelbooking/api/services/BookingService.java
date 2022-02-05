package com.hariansyah.hotelbooking.api.services;

import com.hariansyah.hotelbooking.api.entities.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService extends CommonJDBCService<Booking, Integer> {
    public Long findNumberOfBooked(LocalDate checkInRequest, LocalDate checkOutRequest, Integer roomId);

    List<Booking> findAllBookingByHotelWithTimeRange(LocalDate checkInRequest, LocalDate checkOutRequest, Integer hotelId);

    List<Booking> findAllBookingByRoomWithTimeRange(LocalDate checkInRequest, LocalDate checkOutRequest, Integer hotelId);

    List<Booking> findAllBookingByHotelAllTime(Integer hotelId);

    List<Booking> findAllBookingByRoomAllTime(Integer roomId);
}
