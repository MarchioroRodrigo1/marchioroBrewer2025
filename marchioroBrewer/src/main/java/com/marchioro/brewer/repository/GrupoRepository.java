package com.marchioro.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marchioro.brewer.model.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
   
	Optional<Grupo> findByNomeGrupoIgnoreCase(String nomeGrupo);

}
