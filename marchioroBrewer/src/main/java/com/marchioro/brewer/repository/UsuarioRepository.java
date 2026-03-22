package com.marchioro.brewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.marchioro.brewer.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmailUsuario(String emailUsuario);
	Optional<Usuario> findByEmailUsuario(String emailUsuario);
	List<Usuario> findByAtivoTrue();
	List<Usuario> findByNomeUsuarioContainingIgnoreCaseAndAtivoTrue(String nomeUsuario);
	
	 Page<Usuario> findByNomeUsuarioContainingIgnoreCase(
	            String nomeUsuario,
	            Pageable pageable);
}