package com.marchioro.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Estado;

@Repository
public interface EstadoRepository  extends JpaRepository<Estado, Long>{
	
	  List<Estado> findByRegiaoIdAndAtivoTrue(Long regiaoId);

}
