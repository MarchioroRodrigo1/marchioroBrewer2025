package com.marchioro.brewer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marchioro.brewer.model.Estilo;
import com.marchioro.brewer.repository.EstiloRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/estilos")
public class EstilosController {
	
	private final EstiloRepository estiloRepository;

	public EstilosController(EstiloRepository estiloRepository) {
		this.estiloRepository = estiloRepository;
	}

	@PostMapping("/novo")
	public ResponseEntity<?> salvar (@Valid @RequestBody Estilo estilo){
		boolean existe = estiloRepository.existsByNomeIgnoreCase(estilo.getNome());
		
		if(existe) {
			return ResponseEntity
					.badRequest()
					.body("Já existe um estilo com esse nome.");
		}
		Estilo salvo = estiloRepository.save(estilo);
		
		return ResponseEntity.ok(salvo); // devolve JSON do estilo criado
	}
}
