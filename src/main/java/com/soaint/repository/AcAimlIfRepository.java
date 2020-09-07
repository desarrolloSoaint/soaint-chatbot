package com.soaint.repository;

import com.soaint.entity.AcAimlIf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcAimlIfRepository extends JpaRepository<AcAimlIf, Long>{

    Optional<AcAimlIf> findByAimlIf(String aimlIf);
    boolean existsByAimlIf(String aimlIf);

}
