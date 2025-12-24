package com.marchioro.brewer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.marchioro.brewer.model.Cerveja;


public interface CervejasRepository extends JpaRepository<Cerveja, Long> {

	 Page<Cerveja> findByAtivoTrue(Pageable pageable);

	    Page<Cerveja> findByNomeContainingIgnoreCaseAndAtivoTrue(
	            String nome,
	            Pageable pageable
	    );

	    Page<Cerveja> findByNomeContainingIgnoreCaseAndEstiloIdAndAtivoTrue(
	            String nome,
	            Long estiloId,
	            Pageable pageable
	    );

	    Page<Cerveja> findByEstiloIdAndAtivoTrue(
	            Long estiloId,
	            Pageable pageable
	    );
	}