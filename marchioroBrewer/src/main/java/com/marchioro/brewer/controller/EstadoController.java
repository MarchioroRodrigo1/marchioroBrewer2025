package com.marchioro.brewer.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marchioro.brewer.model.Estado;
import com.marchioro.brewer.repository.EstadoRepository;


@RestController
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoRepository estadoRepository;

    public EstadoController(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @GetMapping("/por-regiao/{regiaoId}")
    public List<Estado> buscarPorRegiao(@PathVariable Long regiaoId) {
    	return estadoRepository.findByRegiaoIdAndAtivoTrue(regiaoId);
    }
}

