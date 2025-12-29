package com.marchioro.brewer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.marchioro.brewer.model.Estilo;
import com.marchioro.brewer.repository.EstiloRepository;

@Controller
@RequestMapping("/estilos")
public class EstilosController {

    private final EstiloRepository estiloRepository;

    public EstilosController(EstiloRepository estiloRepository) {
        this.estiloRepository = estiloRepository;
    }

    @GetMapping
    public ModelAndView listar(@RequestParam(required = false) String nome) {

        ModelAndView mv = new ModelAndView("estilo/ListarEstilos");

        if (nome == null || nome.isBlank()) {
            mv.addObject("estilos", estiloRepository.findByAtivoTrue());
        } else {
            mv.addObject(
                "estilos",
                estiloRepository.findByAtivoTrueAndNomeContainingIgnoreCase(nome)
            );
            mv.addObject("nome", nome);
        }

        return mv;
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {

        Estilo estilo = estiloRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Estilo não encontrado"));

        estilo.setAtivo(false);
        estiloRepository.save(estilo);

        return ResponseEntity.ok().build();
    }
}
