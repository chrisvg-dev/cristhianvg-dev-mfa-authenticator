package com.cristhianvg.MFAGoogleAuthenticator.services;

import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.MFAValidationException;
import com.google.zxing.WriterException;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.apache.http.auth.InvalidCredentialsException;

import java.io.IOException;
import java.util.List;

public interface IMFAuthenticationService extends ICredentialRepository {
    MFAValidationResponse validateMFAuthenticationCode(String mfaCode);
    MFAValidationResponse validateAndActivateMFAuthentication(List<String> codes) throws MFAValidationException;
    String createQRCode() throws WriterException, IOException, InvalidCredentialsException, MFAValidationException;
}
