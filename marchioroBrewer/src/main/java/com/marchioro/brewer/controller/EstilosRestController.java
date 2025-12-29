package com.marchioro.brewer.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.marchioro.brewer.model.Estilo;
import com.marchioro.brewer.repository.EstiloRepository;

@RestController
@RequestMapping("/estilos")
public class EstilosRestController {

    private final EstiloRepository estiloRepository;

    public EstilosRestController(EstiloRepository estiloRepository) {
        this.estiloRepository = estiloRepository;
    }

    @PostMapping("/novo")
    public ResponseEntity<?> salvar(@Valid @RequestBody Estilo estilo) {

        if (estiloRepository.existsByNomeIgnoreCase(estilo.getNome())) {
            return ResponseEntity
                    .badRequest()
                    .body("Já existe um estilo com esse nome.");
        }

        estilo.setAtivo(true);
        Estilo salvo = estiloRepository.save(estilo);

        return ResponseEntity.ok(salvo);
    }
}

