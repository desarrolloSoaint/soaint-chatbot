package com.soaint.repository;

import com.soaint.entity.StaCivil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StaCivilRepository extends JpaRepository<StaCivil, Long>, JpaSpecificationExecutor<StaCivil> {
    Optional<StaCivil> findByName(String name);
    boolean existsByName(String name);
}
