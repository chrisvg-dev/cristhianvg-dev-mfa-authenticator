package com.cristhianvg.MFAGoogleAuthenticator.controllers;

import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAResponse;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationRequest;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.MFAValidationException;
import com.cristhianvg.MFAGoogleAuthenticator.services.IMFAuthenticationService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mfa")
public class MFAuthenticationController {
    private final IMFAuthenticationService authenticationService;

    @GetMapping("/qrcode")
    public ResponseEntity<MFAResponse> createQRCode() throws Exception {
        return ResponseEntity.ok(MFAResponse.builder().qrCode(authenticationService.createQRCode()).build());
    }

    @PostMapping("/validate")
    public ResponseEntity<MFAValidationResponse> validateMFACode(@RequestBody MFAValidationRequest request) {
        return ResponseEntity.ok(this.authenticationService.validateMFAuthenticationCode(request.getCodes().get(0)));
    }

    @PostMapping("/validateAndEnable")
    public ResponseEntity<MFAValidationResponse> validateAndEnableMFA(@RequestBody MFAValidationRequest request) throws MFAValidationException {
        return ResponseEntity.ok(this.authenticationService.validateAndActivateMFAuthentication(request.getCodes()));
    }
}