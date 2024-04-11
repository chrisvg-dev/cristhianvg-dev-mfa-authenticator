package com.cristhianvg.MFAGoogleAuthenticator.repository;

import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IRoleRepository extends JpaRepository<CustomUserRole, UUID> {
}
