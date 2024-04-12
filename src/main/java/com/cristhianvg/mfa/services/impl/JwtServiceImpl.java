package com.cristhianvg.mfa.services.impl;

import com.cristhianvg.mfa.services.IJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class JwtServiceImpl implements IJwtService {
    public static final long JWT_TOKEN_VALIDITY = 60;
    public static final String JWT_SECRET= "671491AE98362741F722202EED3288E8FF2508B35315ADBF75EEB3195A926B40";

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final var claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDateFromToken(String token) {
        return this.getClaimsFromToken(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(String token) {
        final var expirationDate = this.getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public String extractUsername(String token) {
        return this.getClaimsFromToken(token, Claims::getSubject);
    }

    public  String generateToken(UserDetails userDetails) {
        String roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Map<String, Object> claims = Collections.singletonMap("ROLES", roles);
        return this.getToken(claims, userDetails.getUsername());
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                //.setSigningKey(Base64.getEncoder().encode(JWT_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        final Map<String, Object> claims = Collections.singletonMap("ROLES", userDetails.getAuthorities().toString());
        Map<String, Object> fullClaims = new HashMap<>();
        fullClaims.putAll(claims);
        fullClaims.putAll(extraClaims);

        return Jwts.builder()
                .setClaims(fullClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getKey())
                .compact();
    }
    private String getToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 10000))
                .signWith(getKey())
                .compact();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final var usernameFromUserDetails  = userDetails.getUsername();
        final var usernameFromJWT  = this.extractUsername(token);

        return (usernameFromUserDetails.equals(usernameFromJWT)) && !this.isTokenExpired(token);
    }

    private Key getKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secret = Base64.getEncoder().encode(JWT_SECRET.getBytes());
        return new SecretKeySpec(secret, signatureAlgorithm.getJcaName());
    }
    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        // Obtener el encabezado "Authorization" de la solicitud
        String bearerToken = request.getHeader("Authorization");
        // Verificar si el encabezado contiene el prefijo "Bearer" y extraer el JWT
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Eliminar el prefijo "Bearer " para obtener solo el token
        }
        return null;
    }
}