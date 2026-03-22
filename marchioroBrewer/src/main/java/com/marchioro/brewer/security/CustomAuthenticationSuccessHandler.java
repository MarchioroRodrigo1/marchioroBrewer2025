package com.marchioro.brewer.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        String redirectURL = "/";

        // PRIORIDADE DE PERMISSÕES
        if (hasAuthority(authorities, "CADASTRAR_USUARIO")) {
            redirectURL = "/usuario/novo";

        } else if (hasAuthority(authorities, "REALIZAR_VENDA")) {
            redirectURL = "/vendas";

        } else if (hasAuthority(authorities, "LISTAR_CERVEJA")) {
            redirectURL = "/cervejas";

        } else if (hasAuthority(authorities, "LISTAR_CLIENTE")) {
            redirectURL = "/clientes";
        }

        response.sendRedirect(redirectURL);
    }

    private boolean hasAuthority(
            Collection<? extends GrantedAuthority> authorities,
            String authority) {

        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(authority));
    }
}