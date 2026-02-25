package com.marchioro.brewer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.marchioro.brewer.model.Usuario;
import com.marchioro.brewer.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	private final UsuarioRepository usuarioRepository;
	
	
	
	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}



	public Page<Usuario> listar(String nomeUsuario, Pageable pageable) {

	    if (nomeUsuario == null || nomeUsuario.isBlank()) {
	        return usuarioRepository.findAll(pageable);
	    }

	    return usuarioRepository
	            .findByNomeUsuarioContainingIgnoreCase(nomeUsuario, pageable);
	}

}
