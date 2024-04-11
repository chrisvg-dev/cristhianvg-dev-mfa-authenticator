package com.cristhianvg.MFAGoogleAuthenticator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class CustomUserProfile extends CustomBaseEntity {
    @Column(name = "profile_name")
    private String profileName;
    @Column(name = "profile_description")
    private String profileDescription;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_by_profile",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<CustomUserRole> roles;
}