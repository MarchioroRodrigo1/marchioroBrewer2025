package com.marchioro.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Estilo;

@Repository
public interface EstiloRepository extends JpaRepository<Estilo, Long>{
	
	boolean existsByNomeIgnoreCase(String nome);
	
	List<Estilo> findByAtivoTrueAndNomeContainingIgnoreCase(String nome);

	List<Estilo> findByAtivoTrue();

    List<Estilo> findByNomeContainingIgnoreCaseAndAtivoTrue(String nome);

	//List<Estilo> findByAtivoTrueOrderByNomeAsc();

}
