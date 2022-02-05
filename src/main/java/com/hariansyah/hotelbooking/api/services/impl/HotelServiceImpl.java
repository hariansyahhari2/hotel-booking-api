package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.Hotel;
import com.hariansyah.hotelbooking.api.repositories.HotelRepository;
import com.hariansyah.hotelbooking.api.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository repository;

    @Override
    public List<Hotel> findAll() {
        return repository.findAll();
    }

    @Override
    public Hotel findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(Hotel entity) {
        return repository.save(entity);
    }

    @Override
    public Boolean edit(Hotel entity) {
        return repository.edit(entity);
    }

    @Override
    public Boolean removeById(Integer id) {
        return repository.remove(id);
    }
}
