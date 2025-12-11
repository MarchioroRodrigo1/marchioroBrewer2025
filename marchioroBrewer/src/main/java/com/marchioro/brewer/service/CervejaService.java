package com.marchioro.brewer.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.model.Estilo;
import com.marchioro.brewer.repository.CervejasRepository;
import com.marchioro.brewer.repository.EstiloRepository;

@Service
public class CervejaService {

    private final CervejasRepository repository;
    private final EstiloRepository estiloRepository;

    // Injeta também o EstiloRepository para buscar a entidade gerenciada
    public CervejaService(CervejasRepository repository, EstiloRepository estiloRepository) {
        this.repository = repository;
        this.estiloRepository = estiloRepository;
    }

    @Transactional
    public void salvar(Cerveja cerveja) {
        // Se o formulário trouxe apenas estilo.id (binding com estilo.id),
        // aqui garantimos que a cerveja vai ter um Estilo gerenciado pelo JPA.
        if (cerveja.getEstilo() != null && cerveja.getEstilo().getId() != null) {
            Long estiloId = cerveja.getEstilo().getId();
            Optional<Estilo> estiloOpt = estiloRepository.findById(estiloId);
            if (estiloOpt.isPresent()) {
                cerveja.setEstilo(estiloOpt.get());
            } else {
                // Se quiser, lance exceção ou trate como erro de validação.
                throw new IllegalArgumentException("Estilo inválido: " + estiloId);
            }
        } else {
            // Se estilo for obrigatório, pode lançar uma validação aqui
            // throw new IllegalArgumentException("Estilo é obrigatório");
        }

        // Agora salva: repository.save() persiste a cerveja com o Estilo gerenciado
        repository.save(cerveja);
    }
}
