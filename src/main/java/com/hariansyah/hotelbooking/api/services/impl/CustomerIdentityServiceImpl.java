package com.hariansyah.hotelbooking.api.services.impl;

import com.hariansyah.hotelbooking.api.entities.CustomerIdentity;
import com.hariansyah.hotelbooking.api.services.CustomerIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerIdentityServiceImpl implements CustomerIdentityService {

    @Autowired
    private JpaRepository<CustomerIdentity, Integer> repository;

    @Override
    public CustomerIdentity save(CustomerIdentity entity) {
        return repository.save(entity);
    }

    @Override
    public CustomerIdentity removeById(Integer id) {
        CustomerIdentity entity = findById(id);
        if(entity != null) {
            repository.deleteById(id);
            return entity;
        } else {
            return null;
        }
    }

    @Override
    public CustomerIdentity findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<CustomerIdentity> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<CustomerIdentity> findAll(CustomerIdentity search, int page, int size, Sort.Direction direction) {
        Sort sort = Sort.Direction.DESC.equals(direction) ? Sort.by(direction, "id") : Sort.by("id");
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return repository.findAll(
                Example.of(search, matcher),
                PageRequest.of(page, size, sort)
        );
    }
}
