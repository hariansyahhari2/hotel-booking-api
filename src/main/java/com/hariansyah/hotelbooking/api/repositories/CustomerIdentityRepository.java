package com.hariansyah.hotelbooking.api.repositories;

import com.hariansyah.hotelbooking.api.entities.CustomerIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerIdentityRepository extends JpaRepository<CustomerIdentity, Integer> {
}
