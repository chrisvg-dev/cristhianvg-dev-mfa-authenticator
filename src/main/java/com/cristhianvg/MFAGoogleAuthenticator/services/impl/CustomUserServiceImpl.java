package com.cristhianvg.MFAGoogleAuthenticator.services.impl;

import com.cristhianvg.MFAGoogleAuthenticator.config.SecurityConfiguration;
import com.cristhianvg.MFAGoogleAuthenticator.dto.AuthLoginDto;
import com.cristhianvg.MFAGoogleAuthenticator.dto.BaseResponse;
import com.cristhianvg.MFAGoogleAuthenticator.dto.AuthCustomUserDto;
import com.cristhianvg.MFAGoogleAuthenticator.dto.JwtAuthenticationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUser;
import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUserDetails;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.CustomUserException;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IUserRepository;
import com.cristhianvg.MFAGoogleAuthenticator.services.ICustomUserService;
import com.cristhianvg.MFAGoogleAuthenticator.services.IJwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements ICustomUserService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserServiceImpl.class);
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJwtService jwtService;


    @Override
    public BaseResponse saveNewUser(AuthCustomUserDto request) throws CustomUserException {
        CustomUser customUser = this.userRepository.findByEmail(request.getEmail()).orElse(null);

        if (Objects.nonNull(customUser)) {
            throw new CustomUserException("Email already exists with another user.");
        }

        var encryptedPassword = this.passwordEncoder.encode(request.getPassword());
        CustomUser newUser = new CustomUser();
        newUser.setEmail(request.getEmail());
        newUser.setName(request.getName());
        newUser.setLastName(request.getLastName());
        newUser.setPassword(encryptedPassword);

        CustomUser saved = this.userRepository.save(newUser);

        if (saved.getUuid() != null) {
            BaseResponse response = new BaseResponse();
            response.setMessage("User created successfully.");
            response.setHttpStatus(HttpStatus.OK);

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

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        var jwt = jwtService.generateToken(CustomUserDetails.build(user));
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), CustomUserDetails.build(user));

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, refreshToken, user.isMFAEnabled());
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Successfully authenticated");
        return response;
    }
}
