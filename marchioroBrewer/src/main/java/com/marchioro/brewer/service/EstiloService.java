package com.marchioro.brewer.service;
import org.springframework.stereotype.Service;

import com.marchioro.brewer.model.Estilo;
import com.marchioro.brewer.repository.EstiloRepository;

import jakarta.transaction.Transactional;

@Service
public class EstiloService {

    private final EstiloRepository estiloRepository;

    public EstiloService(EstiloRepository estiloRepository) {
        this.estiloRepository = estiloRepository;
    }

    @Transactional
    public Estilo salvar(Estilo estilo) {
        estilo.setAtivo(true);
        return estiloRepository.save(estilo);
    }

    @Transactional
    public void excluir(Long id) {
        Estilo estilo = estiloRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Estilo não encontrado"));
        estilo.setAtivo(false);
    }
}
