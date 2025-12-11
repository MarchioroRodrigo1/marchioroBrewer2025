package com.marchioro.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marchioro.brewer.model.Estilo;

@Repository
public interface EstiloRepository extends JpaRepository<Estilo, Long>{

}
