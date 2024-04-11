package com.cristhianvg.MFAGoogleAuthenticator.repository;

import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByEmail(String email);
}