package com.marchioro.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{
	 List<Cidade> findByAtivoTrue();

}
