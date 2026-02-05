package com.marchioro.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Regiao;

@Repository
public interface RegiaoRepository extends JpaRepository<Regiao, Long>{
	


}
