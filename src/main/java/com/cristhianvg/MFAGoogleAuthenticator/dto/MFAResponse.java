package com.cristhianvg.MFAGoogleAuthenticator.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class MFAResponse {
    private String qrCode;
}
