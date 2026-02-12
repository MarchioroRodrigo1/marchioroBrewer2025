package com.marchioro.brewer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marchioro.brewer.model.Cidade;
import com.marchioro.brewer.repository.CidadeRepository;

@RestController
@RequestMapping("/cidades")
public class CidadeRestController {
	
	private final CidadeRepository cidadeRepository;
	
	public CidadeRestController(CidadeRepository cidadeRepository) {
		this.cidadeRepository = cidadeRepository;
	}

	@GetMapping("/por-estado/{estadoId}")
	public List<Cidade> buscarPorCidade(@PathVariable Long estadoId){
		return cidadeRepository.findByEstadoIdAndAtivoTrue(estadoId);
	}

}
