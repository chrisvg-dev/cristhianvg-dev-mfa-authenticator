package com.cristhianvg.mfa.services.impl;

import com.cristhianvg.mfa.dto.AuthLoginDto;
import com.cristhianvg.mfa.dto.BaseResponse;
import com.cristhianvg.mfa.dto.AuthCustomUserDto;
import com.cristhianvg.mfa.dto.JwtAuthenticationResponse;
import com.cristhianvg.mfa.entities.CustomUser;
import com.cristhianvg.mfa.entities.CustomUserDetails;
import com.cristhianvg.mfa.entities.UserPermission;
import com.cristhianvg.mfa.entities.enums.EPermission;
import com.cristhianvg.mfa.exceptions.CustomUserException;
import com.cristhianvg.mfa.repository.IPermissionDao;
import com.cristhianvg.mfa.repository.IUserRepository;
import com.cristhianvg.mfa.services.ICustomUserService;
import com.cristhianvg.mfa.services.IJwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationServiceImpl implements ICustomUserService {
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationServiceImpl.class);
    private final IUserRepository userRepository;
    private final IPermissionDao permissionDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;


    @Override
    public BaseResponse saveNewUser(AuthCustomUserDto request) throws CustomUserException {
        CustomUser customUser = this.userRepository.findByEmail(request.getEmail()).orElse(null);

        UserPermission userPermission = this.permissionDao.findByPermissionName(EPermission.USERS_READ)
                .orElseThrow(() -> new CustomUserException("Could not find user permission"));

        if (Objects.nonNull(customUser)) {
            throw new CustomUserException("Email already exists with another user.");
        }

        var encryptedPassword = this.passwordEncoder.encode(request.getPassword());
        CustomUser newUser = new CustomUser();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setLastName(request.getLastName());
        newUser.setPassword(encryptedPassword);
        newUser.getPermissions().add(userPermission);

        CustomUser saved = this.userRepository.save(newUser);

        if (saved.getUuid() != null) {
            BaseResponse response = new BaseResponse();
            response.setMessage("User created successfully.");
            return response;
        }

        throw new CustomUserException("Could not create user.");
    }

    @Override
    public JwtAuthenticationResponse login(AuthLoginDto request) throws CustomUserException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Login attempt started");
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        log.info("Login by user: {}", user.getEmail());
        var jwt = jwtService.generateToken(CustomUserDetails.build(user));
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), CustomUserDetails.build(user));
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, refreshToken, user.isMFAEnabled());
        response.setMessage("Successfully authenticated");
        log.info("Login attempt completed");
        return response;
    }
}
