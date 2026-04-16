package com.marchioro.brewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.marchioro.brewer.security.CustomAuthenticationSuccessHandler;
import com.marchioro.brewer.service.AppUserDetailsService;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            PasswordEncoder passwordEncoder,
            AppUserDetailsService userDetailsService) throws Exception {

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .authorizeHttpRequests(auth -> auth

        	    .requestMatchers("/login", "/css/**", "/js/**", "/images/**")
        	    .permitAll()

        	    // =========================
        	    // AVATAR
        	    // =========================
        	    .requestMatchers("/usuario/avatar/**")
        	    .hasAuthority("ALTERAR_AVATAR")

        	    // =========================
        	    // USUÁRIOS
        	    // =========================
        	    .requestMatchers("/usuario/novo")
        	    .hasAuthority("CADASTRAR_USUARIO")

        	    .requestMatchers("/usuario/**")
        	    .hasAuthority("LISTAR_USUARIO")

        	    // =========================
        	    // CERVEJAS 
        	    // =========================
        	 // =========================
        	 // NOVA
        	 // =========================
        	 .requestMatchers(HttpMethod.GET, "/cervejas/novo")
        	 .hasAuthority("CADASTRAR_CERVEJA")

        	 .requestMatchers(HttpMethod.POST, "/cervejas/novo")
        	 .hasAuthority("CADASTRAR_CERVEJA")

        	 // =========================
        	 // EDITA
        	 // =========================
        	 .requestMatchers(HttpMethod.GET, "/cervejas/{id}")
        	 .hasAuthority("EDITAR_CERVEJA")

        	 .requestMatchers(HttpMethod.POST, "/cervejas/{id}")
        	 .hasAuthority("EDITAR_CERVEJA")

        	 // =========================
        	 // LISTAR
        	 // =========================
        	 .requestMatchers("/cervejas")
        	 .hasAuthority("LISTAR_CERVEJA")

        	    // =========================
        	    // CLIENTES
        	    // =========================
        	    .requestMatchers("/clientes/novo")
        	    .hasAuthority("CADASTRAR_CLIENTE")

        	    .requestMatchers("/clientes/**")
        	    .hasAuthority("LISTAR_CLIENTE")

        	    // =========================
        	    // VENDAS
        	    // =========================
        	    .requestMatchers("/vendas/nova")
        	    .hasAuthority("REALIZAR_VENDA")

        	    .requestMatchers("/vendas/**")
        	    .hasAuthority("LISTAR_VENDA")

        	    // =========================
        	    // CONFIG
        	    // =========================
        	    .requestMatchers("/grupo/**")
        	    .hasAuthority("GERENCIAR_GRUPO")

        	    .requestMatchers("/permissao/**")
        	    .hasAuthority("GERENCIAR_PERMISSAO")

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