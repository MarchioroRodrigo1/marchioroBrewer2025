package com.marchioro.brewer.security;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities =
                authentication.getAuthorities();

        String redirectURL = "/";

        for (GrantedAuthority auth : authorities) {

            String role = auth.getAuthority();

            if (role.equals("ROLE_ADMINISTRADOR")) {
                redirectURL = "/usuario/novo";
                break;
            }

            if (role.equals("ROLE_VENDEDOR")) {
                redirectURL = "/vendas";
                break;
            }

            if (role.equals("ROLE_ESTOQUE")) {
                redirectURL = "/cervejas";
                break;
            }

            if (role.equals("ROLE_GERENTE")) {
                redirectURL = "/";
                break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}