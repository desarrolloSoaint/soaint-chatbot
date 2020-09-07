package com.soaint.repository;

import com.soaint.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State,Long>, JpaSpecificationExecutor<State> {
    Optional<State> findByName(String name);
    boolean existsByName(String name);
}
