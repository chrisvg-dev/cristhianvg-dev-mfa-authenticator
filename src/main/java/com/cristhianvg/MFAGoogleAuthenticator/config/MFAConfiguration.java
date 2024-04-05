package com.cristhianvg.MFAGoogleAuthenticator.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MFAConfiguration {

    @Bean
    IGoogleAuthenticator createGoogleAuthenticationService() {
        GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig
                .GoogleAuthenticatorConfigBuilder()
                //.setTimeStepSizeInMillis(50000)
                .build();
        return new GoogleAuthenticator(config);
    }
}
