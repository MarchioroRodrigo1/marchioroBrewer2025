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

            // PUBLICO
            .requestMatchers("/login", "/css/**", "/js/**", "/images/**")
            .permitAll()

            // USUARIOS
            .requestMatchers("/usuario/novo")
            .hasAuthority("CADASTRAR_USUARIO")

            .requestMatchers("/usuario/**")
            .hasAuthority("LISTAR_USUARIO")

            // CERVEJAS
            .requestMatchers("/cervejas/novo")
            .hasAuthority("CADASTRAR_CERVEJA")

            .requestMatchers("/cervejas/**")
            .hasAuthority("LISTAR_CERVEJA")

            // CLIENTES
            .requestMatchers("/clientes/novo")
            .hasAuthority("CADASTRAR_CLIENTE")

            .requestMatchers("/clientes/**")
            .hasAuthority("LISTAR_CLIENTE")

            // VENDAS
            .requestMatchers("/vendas/nova")
            .hasAuthority("REALIZAR_VENDA")

            .requestMatchers("/vendas/**")
            .hasAuthority("LISTAR_VENDA")

            // GRUPOS
            .requestMatchers("/grupo/**")
            .hasAuthority("GERENCIAR_GRUPO")

            // PERMISSOES
            .requestMatchers("/permissao/**")
            .hasAuthority("GERENCIAR_PERMISSAO")

            // QUALQUER OUTRA
            .anyRequest()
            .authenticated()
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