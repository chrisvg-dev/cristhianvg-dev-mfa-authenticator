package com.cristhianvg.MFAGoogleAuthenticator.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface IJwtService {
    String generateToken(UserDetails userDetails);
    String getTokenFromRequest(HttpServletRequest request);
    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
}