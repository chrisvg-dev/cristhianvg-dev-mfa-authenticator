package com.cristhianvg.mfa.anotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CustomSecurity {
    String[] authorities();
}