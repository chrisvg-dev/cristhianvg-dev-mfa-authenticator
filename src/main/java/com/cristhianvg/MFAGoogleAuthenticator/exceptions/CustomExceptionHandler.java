package com.cristhianvg.MFAGoogleAuthenticator.exceptions;

import com.cristhianvg.MFAGoogleAuthenticator.dto.BaseResponse;
import com.cristhianvg.MFAGoogleAuthenticator.dto.MFAValidationResponse;
import com.cristhianvg.MFAGoogleAuthenticator.exceptions.dto.ExceptionValidatorDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MFAValidationException.class)
    public MFAValidationResponse handleMFAValidationException(MFAValidationException ex) {
        return MFAValidationResponse.builder().validationStatus(false).message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public BaseResponse handleAuthenticationException(AccessDeniedException ex) {
        return BaseResponse.builder().httpStatus(HttpStatus.UNAUTHORIZED).message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomUserException.class)
    public MFAValidationResponse handleCustomUserException(CustomUserException ex) {
        return MFAValidationResponse.builder().validationStatus(false).message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.GONE)
    @ExceptionHandler(ExpiredJwtException.class)
    public BaseResponse handleJwtException(ExpiredJwtException ex) {
        return BaseResponse.builder().httpStatus(HttpStatus.GONE).message(ex.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionValidatorDto handleInvalidArguments(MethodArgumentNotValidException exception)
    {
        Map<String,String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error-> errorMap.put(error.getField(), error.getDefaultMessage()));
        return ExceptionValidatorDto.builder()
                .key("INVALID_FIELDS")
                .errors(errorMap).build();
    }
}