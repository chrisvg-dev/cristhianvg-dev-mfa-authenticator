package com.cristhianvg.mfa.repository;

import com.cristhianvg.mfa.entities.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
}