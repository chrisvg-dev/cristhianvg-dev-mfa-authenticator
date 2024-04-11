package com.cristhianvg.MFAGoogleAuthenticator.controllers;

import com.cristhianvg.MFAGoogleAuthenticator.dto.MFABaseValidationRequest;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAQRCodeResponse;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationRequest;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.MFAValidationException;
import com.cristhianvg.MFAGoogleAuthenticator.services.IMFAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mfa")
@Slf4j
public class MFAuthenticationController {
    private final IMFAuthenticationService authenticationService;

    @GetMapping("/qrcode")
    public ResponseEntity<MFAQRCodeResponse> createQRCode(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(MFAQRCodeResponse.builder().qrCode(authenticationService.createQRCode(request)).build());
    }

    @PostMapping("/validate")
    public ResponseEntity<MFAValidationResponse> validateMFACode(HttpServletRequest httpServletRequest, @RequestBody MFABaseValidationRequest request) throws MFAValidationException {
        return ResponseEntity.ok(this.authenticationService.validateMFAuthenticationCode(httpServletRequest, request.getFirstCode()));
    }

    @PostMapping("/validateAndEnable")
    public ResponseEntity<MFAValidationResponse> validateAndEnableMFA(HttpServletRequest httpServletRequest, @RequestBody MFAValidationRequest request) throws MFAValidationException {
        return ResponseEntity.ok(this.authenticationService.validateAndActivateMFAuthentication(httpServletRequest, request));
    }
}