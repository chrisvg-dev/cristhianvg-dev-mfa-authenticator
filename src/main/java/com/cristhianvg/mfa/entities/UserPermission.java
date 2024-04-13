package com.cristhianvg.mfa.entities;

import com.cristhianvg.mfa.entities.enums.EPermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "roles")
public class UserPermission extends CustomBaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private EPermission permissionName;
}