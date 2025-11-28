package com.trades.auth.repositories;

import com.trades.auth.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByEmail(String email);
}
