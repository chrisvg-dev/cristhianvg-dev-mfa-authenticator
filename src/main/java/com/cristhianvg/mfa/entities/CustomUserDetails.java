package com.cristhianvg.mfa.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String name;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String name, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.username = email;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static CustomUserDetails build(CustomUser customUser){
        List<SimpleGrantedAuthority> authorities =
                customUser.getPermissions().stream().map(rol -> new SimpleGrantedAuthority(rol.getPermissionName().name())).toList();
        return new CustomUserDetails(customUser.getName(), customUser.getEmail(), customUser.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}