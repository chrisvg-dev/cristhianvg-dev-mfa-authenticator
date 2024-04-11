package com.cristhianvg.MFAGoogleAuthenticator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MFAValidationResponse extends BaseResponse {
    boolean validationStatus;
    boolean isTokenActive;
}