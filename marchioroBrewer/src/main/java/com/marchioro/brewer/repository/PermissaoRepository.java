package com.marchioro.brewer.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marchioro.brewer.model.Permissao;
import com.marchioro.brewer.model.Usuario;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
	Optional<Permissao> findByNomePermissaoIgnoreCase(String nomePermissao);
	
	List<Permissao> findByAtivoTrue();

}
