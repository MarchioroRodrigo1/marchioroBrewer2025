package com.marchioro.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {
	

    // Bean global usado para criptografar senha
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCrypt = padrão Spring Security (Netflix, Amazon, etc)
        return new BCryptPasswordEncoder();
    }

}
