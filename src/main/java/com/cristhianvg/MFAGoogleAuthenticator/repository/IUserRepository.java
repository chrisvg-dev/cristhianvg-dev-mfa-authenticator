package com.cristhianvg.MFAGoogleAuthenticator.repository;

import com.cristhianvg.MFAGoogleAuthenticator.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}