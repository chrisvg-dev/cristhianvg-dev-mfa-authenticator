package com.cristhianvg.MFAGoogleAuthenticator.services;

import com.cristhianvg.MFAGoogleAuthenticator.dto.AuthLoginDto;
import com.cristhianvg.MFAGoogleAuthenticator.dto.BaseResponse;
import com.cristhianvg.MFAGoogleAuthenticator.dto.AuthCustomUserDto;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.CustomUserException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ICustomUserService {
    BaseResponse saveNewUser(AuthCustomUserDto request) throws CustomUserException;
    BaseResponse login(AuthLoginDto request) throws CustomUserException;
}