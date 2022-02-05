package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.ContactPerson;
import com.hariansyah.hotelbooking.api.repositories.ContactPersonRepository;
import com.hariansyah.hotelbooking.api.services.ContactPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    @Autowired
    private ContactPersonRepository repository;

    @Override
    public List<ContactPerson> findAll() {
        return repository.findAll();
    }

    @Override
    public ContactPerson findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Boolean save(ContactPerson entity) {
        return repository.save(entity);
    }

    @Override
    public Boolean edit(ContactPerson entity) {
        return repository.edit(entity);
    }

    @Override
    public Boolean removeById(Integer id) {
        return repository.remove(id);
    }
}
