package com.cristhianvg.mfa.filters;

import com.cristhianvg.mfa.services.IJwtService;
import com.cristhianvg.mfa.services.IUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final IJwtService jwtService;
    private final IUserDetailsService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    //@Transactional(readOnly = true)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtService.getTokenFromRequest(request);
            String email = jwtService.extractUsername(jwt);

            if (Objects.nonNull(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.userDetailsService().loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );

                    token.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof ExpiredJwtException) {
                handlerExceptionResolver.resolveException(request, response, null, e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        //Cookie cookie =  WebUtils.getCookie(request, cookieName);

        //if (Objects.nonNull(cookie) && Objects.nonNull(cookie.getValue())) {
        //    return cookie.getValue();
        //}
        return "";
    }
}