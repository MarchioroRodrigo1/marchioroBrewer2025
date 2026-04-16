package com.marchioro.brewer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marchioro.brewer.model.Venda;
import com.marchioro.brewer.repository.VendaRepository;

@Service
public class VendaService {

    private final VendaRepository repository;

    public VendaService(VendaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void salvar(Venda venda) {

        if (venda.getValorTotal() == null) {
            venda.setValorTotal(
                venda.getValorFrete()
                    .subtract(venda.getValorDesconto())
            );
        }
        
        if (venda.getItens() != null) {
            venda.getItens().forEach(item -> item.setVenda(venda));
        }

        repository.save(venda);
    }
    
    public Page<Venda> listar(Pageable pageable) {
        return repository.findByAtivoTrue(pageable);
    }

    public Venda buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));
    }

    @Transactional
    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        venda.setAtivo(false);
        repository.save(venda);
    }
}