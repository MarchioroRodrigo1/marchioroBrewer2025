package com.marchioro.brewer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marchioro.brewer.model.Cliente;
import com.marchioro.brewer.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // =========================
    // SALVAR
    // =========================
    @Transactional
    public Cliente salvar(Cliente cliente) {

        if (cliente.getId() == null &&
            clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new IllegalArgumentException("Já existe um cliente com este CPF/CNPJ");
        }

        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    // =========================
    // EXCLUSÃO LÓGICA
    // =========================
    @Transactional
    public void excluir(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
    
    public Page<Cliente> listar(Pageable pageable) {
        return clienteRepository.findByAtivoTrue(pageable);
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

}
