package com.hariansyah.hotelbooking.api.repositories;

import com.hariansyah.hotelbooking.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Boolean existsByUsername(String username);
    Account findByUsername(String username);
}
