package com.cristhianvg.MFAGoogleAuthenticator.controllers;

import com.cristhianvg.MFAGoogleAuthenticator.anotations.CustomSecurity;
import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUser;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IUserRepository;
import com.cristhianvg.MFAGoogleAuthenticator.services.ICustomUserService;
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
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasAuthority('READ'))")
    public List<CustomUser> userList() {
        return this.userRepository.findAll();
    }

}
