package com.dkolesnik.springplayground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-25
 */
@Configuration
public class CoreApplicationConfig {

    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {

        /**
         * Create DEFAULT BCRYPT Password Encoder from Spring Security
         */
        return new BCryptPasswordEncoder();
    }
    
}
