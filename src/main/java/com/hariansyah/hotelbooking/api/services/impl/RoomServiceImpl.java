package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.Room;
import com.hariansyah.hotelbooking.api.repositories.RoomRepository;
import com.hariansyah.hotelbooking.api.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository repository;

    @Override
    public List<Room> findAll() {
        return repository.findAll();
    }

    @Override
    public Room findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(Room entity) {
        return repository.save(entity);
    }

    @Override
    public Boolean edit(Room entity) {
        return repository.edit(entity);
    }

    @Override
    public Boolean removeById(Integer id) {
        return repository.remove(id);
    }
}
