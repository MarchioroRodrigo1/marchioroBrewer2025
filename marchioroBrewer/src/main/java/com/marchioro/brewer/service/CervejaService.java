package com.marchioro.brewer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marchioro.brewer.dto.CervejaFiltro;
import com.marchioro.brewer.model.Cerveja;
import com.marchioro.brewer.repository.CervejasRepository;
import com.marchioro.brewer.repository.EstiloRepository;

@Service
public class CervejaService {

    private final CervejasRepository repository;
    private final EstiloRepository estiloRepository;

    public CervejaService(CervejasRepository repository,
                          EstiloRepository estiloRepository) {
        this.repository = repository;
        this.estiloRepository = estiloRepository;
    }

    // ------------------------------------------------------------
    // Salvar cerveja garantindo Estilo gerenciado
    // ------------------------------------------------------------    
    @Transactional
    public void salvar(Cerveja cerveja) {

        if (!cerveja.getEstilo().isAtivo()) {
            throw new IllegalStateException("Estilo inativo");
        }

        repository.save(cerveja);
    }

    public Page<Cerveja> filtrar(CervejaFiltro filtro, Pageable pageable){

        String nome = filtro.getNome();
        if (nome != null) {
            nome = nome.trim();
            if (nome.isEmpty()) {
                nome = null;
            }
        }

        Long estilo = filtro.getEstilo();

        boolean temNome = nome != null;
        boolean temEstilo = estilo != null;

        // --------------------------------------------------------
        // Nenhum filtro → lista tudo ATIVO
        // --------------------------------------------------------
        if (!temNome && !temEstilo) {
            return repository.findByAtivoTrue(pageable);
        }

        // --------------------------------------------------------
        // Nome + Estilo
        // --------------------------------------------------------
        if (temNome && temEstilo) {
            return repository
                    .findByNomeContainingIgnoreCaseAndEstiloIdAndAtivoTrue(
                            nome, estilo, pageable);
        }

        // --------------------------------------------------------
        // Apenas Nome
        // --------------------------------------------------------
        if (temNome) {
            return repository
                    .findByNomeContainingIgnoreCaseAndAtivoTrue(nome, pageable);
        }

        // --------------------------------------------------------
        // Apenas Estilo
        // --------------------------------------------------------
        return repository.findByEstiloIdAndAtivoTrue(estilo, pageable);
    }
    
    public Cerveja buscarPorCodigo(Long codigo) {
        return repository.findById(codigo)
                .filter(Cerveja::isAtivo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cerveja não encontrada ou inativa"));
    }

    // ------------------------------------------------------------
    // SOFT DELETE (exclusão lógica)
    // ------------------------------------------------------------
    @Transactional
    public void excluir(Long id) {

        Cerveja cerveja = repository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Cerveja não encontrada"));

        cerveja.setAtivo(false);
    }

}
