package com.marchioro.brewer.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.marchioro.brewer.model.Grupo;
import com.marchioro.brewer.model.Usuario;
import com.marchioro.brewer.repository.UsuarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByEmailUsuario(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado"));

        // =============================
        // CONVERTE GRUPOS → ROLES
        // =============================
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Grupo grupo : usuario.getGrupos()) {

            String role = "ROLE_" +
                    grupo.getNomeGrupo()
                          .toUpperCase()
                          .replace(" ", "_");

            authorities.add(new SimpleGrantedAuthority(role));
        }
        
        //Remover  depos
        System.out.println("ROLES DO USUARIO:");
        System.out.println(authorities);

        // =============================
        // CRIA USER DO SPRING
        // =============================
        
       
        
        return User.builder()
                .username(usuario.getEmailUsuario())
                .password(usuario.getSenhaUsuario())
                .authorities(authorities)
                .accountLocked(!usuario.isAccountNonLocked())
                .disabled(!usuario.isEnabled())
                .build();
    }
    
}