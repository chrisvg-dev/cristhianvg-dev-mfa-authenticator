package com.cristhianvg.mfa.dto;

import lombok.*;

@Getter @Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse extends BaseResponse {
    private String token;
    private String refreshToken;
    private boolean mfa;
}