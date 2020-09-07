package com.soaint.repository;

import com.soaint.entity.AcAiml;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcAimlRepository extends JpaRepository<AcAiml, Long>{

    Optional<AcAiml> findByAiml(String aiml);
    boolean existsByAiml(String aiml);

}
