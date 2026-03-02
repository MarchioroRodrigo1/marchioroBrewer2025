package com.marchioro.brewer.service;

import org.springframework.stereotype.Service;

import com.marchioro.brewer.model.Permissao;
import com.marchioro.brewer.repository.PermissaoRepository;

import jakarta.transaction.Transactional;

@Service

public class PermissaoService {
	
	 private final PermissaoRepository repository;

	    public PermissaoService(PermissaoRepository repository) {
	        this.repository = repository;
	    }

	    @Transactional
	    public Permissao salvar(Permissao permissao) {

	        repository.findByNomePermissaoIgnoreCase(permissao.getNomePermissao())
	            .ifPresent(p -> {
	                if (!p.getId().equals(permissao.getId())) {
	                    throw new IllegalArgumentException("Permissão já cadastrada");
	                }
	            });

	        return repository.save(permissao);
	    }

}
