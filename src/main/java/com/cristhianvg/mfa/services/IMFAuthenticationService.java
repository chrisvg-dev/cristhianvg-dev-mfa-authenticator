package com.cristhianvg.mfa.services;

import com.cristhianvg.mfa.dto.MFAValidationRequest;
import com.cristhianvg.mfa.dto.MFAValidationResponse;
import com.cristhianvg.mfa.exceptions.MFAValidationException;
import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.ICredentialRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.auth.InvalidCredentialsException;

import java.io.IOException;

public interface IMFAuthenticationService extends ICredentialRepository {
    MFAValidationResponse validateMFAuthenticationCode(HttpServletRequest httpServletRequest, String mfaCode) throws MFAValidationException;
    MFAValidationResponse validateAndActivateMFAuthentication(HttpServletRequest httpServletRequest, MFAValidationRequest request) throws MFAValidationException;
    String createQRCode(HttpServletRequest request) throws WriterException, IOException, InvalidCredentialsException, MFAValidationException;
}
