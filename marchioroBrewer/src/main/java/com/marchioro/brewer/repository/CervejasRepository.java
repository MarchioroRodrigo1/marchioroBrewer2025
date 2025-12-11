package com.marchioro.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.marchioro.brewer.model.Cerveja;

public interface CervejasRepository extends JpaRepository<Cerveja, Long> {
}

