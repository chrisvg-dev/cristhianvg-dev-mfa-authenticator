package com.cristhianvg.mfa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "user_permission_mapping")
public class UserPermissionMapping extends CustomBaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser user;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private UserPermission permission;

    private boolean read;
    private boolean write;
    private boolean delete;
    private boolean update;
}