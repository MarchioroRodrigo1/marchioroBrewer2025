package com.marchioro.brewer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marchioro.brewer.model.Grupo;
import com.marchioro.brewer.repository.GrupoRepository;

import jakarta.transaction.Transactional;

@Service
public class GrupoService {

	@Autowired
	private GrupoRepository grupoRepository;
	
	 // ===============================
    // SALVAR
   // ===============================
	
	@Transactional
	public Grupo salvar (Grupo grupo) {
		grupoRepository.findByNomeGrupoIgnoreCase(grupo.getNomeGrupo())
		.ifPresent(g -> {
			if (grupo.getId() == null || !g.getId().equals(grupo.getId())) {
				throw new IllegalArgumentException("Já exite um grupo com este nome");
			}
		});
		return grupoRepository.save(grupo);
	}
	
	 // ===============================
    // LISTAR
   // ===============================
    public List<Grupo> listarAtivos() {
        return grupoRepository.findAll();
    }
    
     // ===============================
    // BUSCAR
   // ===============================
    
    public Grupo buscar(Long id) {
    	return grupoRepository.findById(id)
    			.orElseThrow(() -> 
    			new IllegalArgumentException("Grupo inválido"));
    }
    

    // ===============================
    // DESATIVAR (soft delete)
    // ===============================
    @Transactional
    public void desativar(Long id) {

        Grupo grupo = buscar(id);
        grupo.setAtivo(false);

        grupoRepository.save(grupo);
    }
}
