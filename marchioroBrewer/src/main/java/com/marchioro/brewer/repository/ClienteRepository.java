package com.marchioro.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Validação correta
    boolean existsByDocumento(String cpfCnpj);

    // Listagem padrão (soft delete)
    List<Cliente> findByAtivoTrue();

    // Filtro por nome
    List<Cliente> findByNomeClienteContainingIgnoreCaseAndAtivoTrue(String nomeCliente);
}
