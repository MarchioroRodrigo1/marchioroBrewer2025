package com.marchioro.brewer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Validação correta
    boolean existsByDocumento(String cpfCnpj);

    // Listagem padrão (soft delete)
   // List<Cliente> findByAtivoTrue();
    Page<Cliente> findByAtivoTrue(Pageable pageable);
   

    // Filtro por nome
   // List<Cliente> findByNomeClienteContainingIgnoreCaseAndAtivoTrue(String nomeCliente);
    
    Page<Cliente> findByNomeClienteContainingIgnoreCaseAndAtivoTrue(String nomeCliente, Pageable pageable);
}
   
