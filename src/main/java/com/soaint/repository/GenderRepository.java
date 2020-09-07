package com.soaint.repository;

import com.soaint.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender,Long>, JpaSpecificationExecutor<Gender> {
    Optional<Gender> findByName(String name);
    boolean existsByName(String name);
}
