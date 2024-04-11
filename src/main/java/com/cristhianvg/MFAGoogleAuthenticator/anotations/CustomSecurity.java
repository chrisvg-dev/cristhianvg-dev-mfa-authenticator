package com.cristhianvg.MFAGoogleAuthenticator.anotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CustomSecurity {
    String[] authorities();
}