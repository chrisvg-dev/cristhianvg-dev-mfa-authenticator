package com.cristhianvg.mfa.services;

import com.cristhianvg.mfa.dto.AuthLoginDto;
import com.cristhianvg.mfa.dto.BaseResponse;
import com.cristhianvg.mfa.dto.AuthCustomUserDto;
import com.cristhianvg.mfa.exceptions.CustomUserException;

public interface ICustomUserService {
    BaseResponse saveNewUser(AuthCustomUserDto request) throws CustomUserException;
    BaseResponse login(AuthLoginDto request) throws CustomUserException;
}