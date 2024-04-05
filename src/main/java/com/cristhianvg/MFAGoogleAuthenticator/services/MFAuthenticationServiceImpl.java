package com.cristhianvg.MFAGoogleAuthenticator.services;

import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.entities.User;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.MFAValidationException;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IUserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.ICredentialRepository;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MFAuthenticationServiceImpl implements IMFAuthenticationService {
    public static final String PNG = "PNG";
    public static final String OTP_AUTH_URL_FORMAT = "otpauth://totp/%s:%s?secret=%s&issuer=%s";
    private final IGoogleAuthenticator googleAuthenticatorService;
    private final IUserRepository userRepository;

    @Override
    public MFAValidationResponse validateMFAuthenticationCode(String mfaCode) {
        String accountName = "cristianvg9692@gmail.com";

        boolean status = false;
        String message = "";
        try {
            User user = userRepository.findByEmail(accountName)
                    .orElseThrow(() -> new InvalidCredentialsException("Could not find user " + accountName));

            String secretKey = user.getSecretKey();

            status = googleAuthenticatorService.authorize(secretKey, Integer.parseInt(mfaCode));
            message = "MFA code validation OK";
        } catch (NumberFormatException e) {
            message = "Invalid MFA code. Must be a number.";
        } catch (Exception e) {
            message = "Error during MFA code validation";
        }

        return MFAValidationResponse.builder().status(status).message(message).build();
    }

    @Override
    public MFAValidationResponse validateAndActivateMFAuthentication(List<String> codes) throws MFAValidationException {
        String accountName = "cristianvg9692@gmail.com";
        User user = userRepository.findByEmail(accountName).orElseThrow(() -> new MFAValidationException("Could not find user " + accountName));

        boolean status = false;
        String message = "";

        if (user.isMFAEnabled()) {
            message = "This user already has MFA enabled";
            throw new MFAValidationException(message);
        } else {
            try {
                if (codes == null || codes.isEmpty() || codes.size() > 2) {
                    throw new MFAValidationException("This validation requires 2 mfa codes");
                }

                String secretKey = user.getSecretKey();

                for (String code : codes) {
                    status = googleAuthenticatorService.authorize(secretKey, Integer.parseInt(code));

                    if(!status) {
                        break;
                    }
                }

                if (status) {
                    user.setMFAEnabled(true);
                    userRepository.save(user);
                    message = "MFA code validation OK";
                    return MFAValidationResponse.builder().status(status).message(message).build();
                } else {
                    message = "MFA code validation FAILED.";
                }
            } catch (NumberFormatException e) {
                message = "Invalid MFA codes. Both of them must be a number.";
            }
        }

        throw new MFAValidationException(message);
    }

    @Override
    public String getSecretKey(String userName) {
        return null;
    }

    @Override
    public String createQRCode() throws MFAValidationException {
        try {
            String otpAuthURL = createOTPAuthURL();
            BitMatrix bitMatrix = new MultiFormatWriter().encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
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

    private String createOTPAuthURL() throws InvalidCredentialsException, MFAValidationException {
        String accountName = "cristianvg9692@gmail.com";
        String issuerName = "Moneki";

        this.googleAuthenticatorService.setCredentialRepository(this);
        GoogleAuthenticatorKey googleAuthenticatorKey = this.googleAuthenticatorService.createCredentials(accountName);
        String secretKey = googleAuthenticatorKey.getKey();

        User user = this.userRepository.findByEmail(accountName).orElseThrow(() -> new InvalidCredentialsException("Could not find user " + accountName));

        if (user.isMFAEnabled()) {
            throw new MFAValidationException("User " + accountName + " already has MFA enabled.");
        }

        user.setSecretKey(secretKey);
        user.setMFAEnabled(false); // Awaiting 2 codes validation
        this.userRepository.save(user);

        String urlEncodedIssuer = URLEncoder.encode(issuerName, StandardCharsets.UTF_8);
        String urlEncodedAccount = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        return String.format(OTP_AUTH_URL_FORMAT, urlEncodedIssuer, urlEncodedAccount, secretKey, urlEncodedIssuer);
    }
}
