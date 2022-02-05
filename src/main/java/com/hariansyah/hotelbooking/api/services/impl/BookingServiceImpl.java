package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.Booking;
import com.hariansyah.hotelbooking.api.repositories.BookingRepository;
import com.hariansyah.hotelbooking.api.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository repository;

    @Override
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Override
    public Booking findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(Booking entity) {
        return repository.save(entity);
    }

    @Override
    public Boolean edit(Booking entity) {
        return repository.edit(entity);
    }

    @Override
    public Boolean removeById(Integer id) {
        return repository.remove(id);
    }

    @Override
    public Long findNumberOfBooked(LocalDate checkInRequest, LocalDate checkOutRequest, Integer roomId) {
        return repository.findNumberOfBooked(checkInRequest, checkOutRequest, roomId);
    }

    @Override
    public List<Booking> findAllBookingByHotelWithTimeRange(LocalDate checkInRequest, LocalDate checkOutRequest, Integer hotelId) {
        return repository.findAllBookingByHotelWithTimeRange(checkInRequest, checkOutRequest, hotelId);
    }

    @Override
    public List<Booking> findAllBookingByRoomWithTimeRange(LocalDate checkInRequest, LocalDate checkOutRequest, Integer hotelId) {
        return repository.findAllBookingByRoomWithTimeRange(checkInRequest, checkOutRequest, hotelId);
    }

    @Override
    public List<Booking> findAllBookingByHotelAllTime(Integer hotelId) {
        return repository.findAllBookingByHotelAllTime(hotelId);
    }

    @Override
    public List<Booking> findAllBookingByRoomAllTime(Integer roomId) {
        return repository.findAllBookingbyRoomAllTime(roomId);
    }
}
