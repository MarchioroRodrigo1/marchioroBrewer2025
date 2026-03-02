package com.marchioro.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.marchioro.brewer.security.CustomAuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    http
	        .authorizeHttpRequests(auth -> auth

	            // Público
	            .requestMatchers("/login", "/css/**", "/js/**")
	                .permitAll()

	            // ADMINISTRADOR
	            .requestMatchers("/usuario/**")            
	                .hasAuthority("ROLE_ADMINISTRADOR")
	            .requestMatchers("/cerveja/**")
	                .hasAnyAuthority("ROLE_ADMINISTRADOR")
	            .requestMatchers("/venda/**")
	                .hasAnyAuthority("ROLE_ADMINISTRADOR")
	            .requestMatchers("/grupo/**")
	                .hasAnyAuthority("ROLE_ADMINISTRADOR")


	            // GERENTE
	            .requestMatchers("/cerveja/**")
	                .hasAnyAuthority("ROLE_GERENTE")

	            // VENDAS
	            .requestMatchers("/vendas/**")
	                .hasAuthority("ROLE_VENDEDOR")
	            .requestMatchers("/venda/**")
	                .hasAnyAuthority("ROLE_VENDEDOR")
	            .requestMatchers("/cliente/**")
	                .hasAnyAuthority("ROLE_VENDEDOR")

	            // QUALQUER OUTRO
	            .anyRequest().authenticated()
	        )

	        .formLogin(form -> form
	            .loginPage("/login")
	            .loginProcessingUrl("/login")
	            .successHandler(successHandler)
	            .failureUrl("/login?error")
	            .permitAll()
	        )

	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout")
	        );

	    return http.build();
	}
}