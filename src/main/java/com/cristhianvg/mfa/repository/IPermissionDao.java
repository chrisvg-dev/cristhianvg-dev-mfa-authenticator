package com.cristhianvg.mfa.repository;

import com.cristhianvg.mfa.entities.UserPermission;
import com.cristhianvg.mfa.entities.enums.EPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPermissionDao extends JpaRepository<UserPermission, Long> {
    Optional<UserPermission> findByPermissionName(EPermission permission);
}
