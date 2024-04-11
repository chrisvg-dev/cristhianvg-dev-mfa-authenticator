package com.cristhianvg.MFAGoogleAuthenticator.services.impl;

import com.cristhianvg.MFAGoogleAuthenticator.entities.CustomUserDetails;
import com.cristhianvg.MFAGoogleAuthenticator.repository.IUserRepository;
import com.cristhianvg.MFAGoogleAuthenticator.services.IUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements IUserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> CustomUserDetails.build(
                userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found")));
    }
}
