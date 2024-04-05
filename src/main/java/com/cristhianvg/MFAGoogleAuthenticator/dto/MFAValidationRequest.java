package com.cristhianvg.MFAGoogleAuthenticator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MFAValidationRequest {
    List<String> codes;
}
