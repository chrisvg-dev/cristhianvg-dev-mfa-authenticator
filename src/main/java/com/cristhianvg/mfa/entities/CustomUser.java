package com.cristhianvg.mfa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class CustomUser extends CustomBaseEntity {
    private String name;

    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

    @Column(name = "is_mfa_enabled")
    private boolean isMFAEnabled;

    @Column(name = "secret_key")
    private String secretKey;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_by_user", joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<CustomUserPermission> permissions = new HashSet<>();
}
