package com.cristhianvg.mfa.controllers;

import com.cristhianvg.mfa.entities.CustomUser;
import com.cristhianvg.mfa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class CustomUsersController {
    private final IUserRepository userRepository;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('USERS_READ')")
    public List<CustomUser> userList() {
        return this.userRepository.findAll();
    }

}
