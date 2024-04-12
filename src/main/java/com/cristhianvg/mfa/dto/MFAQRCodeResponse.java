package com.cristhianvg.mfa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MFAQRCodeResponse extends BaseResponse {
    private String qrCode;
}
