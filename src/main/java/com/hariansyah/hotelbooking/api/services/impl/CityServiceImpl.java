package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.City;
import com.hariansyah.hotelbooking.api.repositories.CityRepository;
import com.hariansyah.hotelbooking.api.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository repository;

    @Override
    public List<City> findAll() {
        return repository.findAll();
    }

    @Override
    public City findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(City entity) {
        return repository.save(entity);
    }

    @Override
    public Boolean edit(City entity) {
        return repository.edit(entity);
    }

    @Override
    public Boolean removeById(Integer id) {
        return repository.remove(id);
    }
}
