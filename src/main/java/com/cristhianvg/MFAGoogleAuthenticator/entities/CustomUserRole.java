package com.cristhianvg.MFAGoogleAuthenticator.entities;

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
public class CustomUserRole extends CustomBaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ERole roleName;

    public enum ERole {
        ADMIN, USER, READ, WRITE
    }
}