package com.marchioro.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.marchioro.brewer.model.Cliente;
import com.marchioro.brewer.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    Page<Venda> findByAtivoTrue(Pageable pageable);

    @Query("""
    	    SELECT SUM(v.valorTotal)
    	    FROM Venda v
    	    WHERE v.vendedor.id = :vendedorId
    	    AND v.dataCriacao BETWEEN :inicio AND :fim
    	    AND v.ativo = true
    	""")
    	BigDecimal totalVendasPorPeriodo(
    	    Long vendedorId,
    	    LocalDate inicio,
    	    LocalDate fim
    	);
    
    //Dashboard geral (admin)
    
    @Query("""
    	    SELECT SUM(v.valorTotal)
    	    FROM Venda v
    	    WHERE v.dataCriacao BETWEEN :inicio AND :fim
    	    AND v.ativo = true
    	""")
    	BigDecimal totalGeralPeriodo(LocalDate inicio, LocalDate fim);
    
    //Ranking de vendedores
    @Query("""
    	    SELECT v.vendedor.nomeUsuario, SUM(v.valorTotal)
    	    FROM Venda v
    	    WHERE v.ativo = true
    	    GROUP BY v.vendedor.nomeUsuario
    	""")
    	List<Object[]> rankingVendedores();
    	
    
}
