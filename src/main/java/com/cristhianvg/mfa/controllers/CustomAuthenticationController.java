package com.cristhianvg.mfa.controllers;

import com.cristhianvg.mfa.dto.AuthLoginDto;
import com.cristhianvg.mfa.dto.BaseResponse;
import com.cristhianvg.mfa.dto.AuthCustomUserDto;
import com.cristhianvg.mfa.exceptions.CustomUserException;
import com.cristhianvg.mfa.services.ICustomUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class CustomAuthenticationController {
    private final ICustomUserService userService;

    @PostMapping("/registerNewUser")
    public ResponseEntity<BaseResponse> registerNewUser(@Valid @RequestBody AuthCustomUserDto request) throws CustomUserException {
        return ResponseEntity.ok(userService.saveNewUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody AuthLoginDto request) throws CustomUserException {
        return ResponseEntity.ok(userService.login(request));
    }
}
