package com.cristhianvg.mfa.repository;

import com.cristhianvg.mfa.entities.CustomUserPermission;
import com.cristhianvg.mfa.entities.enums.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionDao extends JpaRepository<CustomUserPermission, Long> {
    Optional<CustomUserPermission> findByPermissionName(EPermission permission);
}
