package com.cristhianvg.mfa.services.impl;

import com.cristhianvg.mfa.entities.CustomUserDetails;
import com.cristhianvg.mfa.repository.IUserRepository;
import com.cristhianvg.mfa.services.IUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements IUserDetailsService {
    private final IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetailsService userDetailsService() {
        return username -> CustomUserDetails.build(
                userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username not found")));
    }
}
