package com.yar.iot.queryportal.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application security configuration
 * */
@Configuration
public class AppSecurityConfig {

    /**
     * Create the custom {@link PasswordEncoder} bean
     *
     * @return {@link PasswordEncoder}
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Create the custom {@link SecurityAuditorAware} bean
     *
     * @return {@link SecurityAuditorAware}
     * */
    @Bean
    public SecurityAuditorAware securityAuditorAware() {
        return new SecurityAuditorAware();
    }
}
