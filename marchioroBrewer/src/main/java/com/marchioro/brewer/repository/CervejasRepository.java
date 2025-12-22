package com.marchioro.brewer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.marchioro.brewer.model.Cerveja;


public interface CervejasRepository extends JpaRepository<Cerveja, Long> {


	    Page<Cerveja> findByNomeContainingIgnoreCase(
	            String nome,
	            Pageable pageable
	    );

	    Page<Cerveja> findByNomeContainingIgnoreCaseAndEstiloId(
	            String nome,
	            Long estiloId,
	            Pageable pageable
	    );
	

}

