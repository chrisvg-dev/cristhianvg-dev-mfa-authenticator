package com.cristhianvg.mfa.services.impl;

import com.cristhianvg.mfa.dto.MFAValidationRequest;
import com.cristhianvg.mfa.dto.MFAValidationResponse;
import com.cristhianvg.mfa.entities.CustomUser;
import com.cristhianvg.mfa.exceptions.MFAValidationException;
import com.cristhianvg.mfa.repository.IUserRepository;
import com.cristhianvg.mfa.services.IJwtService;
import com.cristhianvg.mfa.services.IMFAuthenticationService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MFAuthenticationServiceImpl implements IMFAuthenticationService {
    public static final String PNG = "PNG";
    public static final String OTP_AUTH_URL_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
    public static final String COULD_NOT_FIND_USER = "Could not find user ";
    private final IGoogleAuthenticator googleAuthenticatorService;
    private final IUserRepository userRepository;
    private final IJwtService jwtService;

    @Override
    public MFAValidationResponse validateMFAuthenticationCode(HttpServletRequest httpServletRequest, String mfaCode) throws MFAValidationException {

        if (jwtService.isTokenExpired(this.jwtService.getTokenFromRequest(httpServletRequest))) {
            throw new MFAValidationException("Token expired");
        }
        String jwt = this.jwtService.getTokenFromRequest(httpServletRequest);
        String accountName = obtainUsername(httpServletRequest);

        try {
            CustomUser user = userRepository.findByEmail(accountName)
                    .orElseThrow(() -> new MFAValidationException(COULD_NOT_FIND_USER + accountName));

            String secretKey = user.getSecretKey();

            boolean status = googleAuthenticatorService.authorize(secretKey, Integer.parseInt(mfaCode));
            return MFAValidationResponse.builder()
                    .validationStatus(status)
                    .validationStatus(true)
                    .isTokenActive(!this.jwtService.isTokenExpired(jwt))
                    .message("MFA code validation OK")
                    .mfaEnabled(user.isMFAEnabled())
                    .mfaAuthenticated(true)
                    .build();
        } catch (NumberFormatException e) {
            throw new MFAValidationException("Invalid MFA code. Must be a number.");
        }
    }

    @Override
    public MFAValidationResponse validateAndActivateMFAuthentication(HttpServletRequest httpServletRequest, MFAValidationRequest request) throws MFAValidationException {
        String accountName = obtainUsername(httpServletRequest);
        CustomUser user = userRepository.findByEmail(accountName).orElseThrow(() -> new MFAValidationException(COULD_NOT_FIND_USER + accountName));

        boolean status = false;

        if (user.isMFAEnabled()) {
            throw new MFAValidationException("This user already has MFA enabled");
        } else {
            try {
                if (request == null || request.getFirstCode().isEmpty() ||  request.getSecondCode().isEmpty()) {
                    throw new MFAValidationException("This validation requires 2 mfa codes");
                }

                String jwt = this.jwtService.getTokenFromRequest(httpServletRequest);

                String secretKey = user.getSecretKey();

                status = googleAuthenticatorService.authorize(secretKey, Integer.parseInt(request.getFirstCode())) &&
                         googleAuthenticatorService.authorize(secretKey, Integer.parseInt(request.getSecondCode()));

                if (status) {
                    user.setMFAEnabled(true);
                    userRepository.save(user);
                    return MFAValidationResponse
                            .builder()
                            .isTokenActive(!this.jwtService.isTokenExpired(jwt))
                            .validationStatus(status)
                            .message("MFA code validation OK")
                            .mfaEnabled(user.isMFAEnabled())
                            .mfaAuthenticated(true)
                            .build();
                } else {
                    throw new MFAValidationException("MFA code validation FAILED.");
                }
            } catch (NumberFormatException e) {
                throw new MFAValidationException("Invalid MFA codes. Both of them must be a number.");
            }
        }
    }

    @Override
    public String getSecretKey(String userName) {
        return null;
    }

    @Override
    public String createQRCode(HttpServletRequest request) throws MFAValidationException {
        try {
            String otpAuthURL = createOTPAuthURL(request);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(otpAuthURL, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, PNG, outputStream);
            return Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception ex) {
            throw new MFAValidationException(ex.getMessage());
        }
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {

    }

    private String createOTPAuthURL(HttpServletRequest request) throws InvalidCredentialsException, MFAValidationException {
        String accountName = obtainUsername(request);
        String issuerName = "Moneki";

        this.googleAuthenticatorService.setCredentialRepository(this);


        CustomUser user = this.userRepository.findByEmail(accountName).orElseThrow(() -> new InvalidCredentialsException(COULD_NOT_FIND_USER + accountName));

        if (user.isMFAEnabled()) {
            throw new MFAValidationException("User " + accountName + " already has MFA enabled.");
        }

        if (Objects.isNull(user.getSecretKey()) || user.getSecretKey().isEmpty()) {
            GoogleAuthenticatorKey googleAuthenticatorKey = this.googleAuthenticatorService.createCredentials(accountName);
            String secretKey = googleAuthenticatorKey.getKey();
            user.setSecretKey(secretKey);
        }

        user.setMFAEnabled(false); // Awaiting 2 codes validation
        this.userRepository.save(user);

        String urlEncodedIssuer = URLEncoder.encode(issuerName, StandardCharsets.UTF_8);
        String urlEncodedAccount = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        return String.format(OTP_AUTH_URL_FORMAT, urlEncodedIssuer, urlEncodedAccount, user.getSecretKey(), urlEncodedIssuer);
    }

    private String obtainUsername(HttpServletRequest request) throws MFAValidationException {
        String jwt = this.jwtService.getTokenFromRequest(request);
        String accountName = this.jwtService.extractUsername(jwt);

        if (ObjectUtils.isEmpty(accountName)) {
            throw new MFAValidationException("No username was found");
        }

        return accountName;
    }
}
