package com.soaint.repository;

import com.soaint.entity.AcQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface AcQuizRepository extends JpaRepository<AcQuiz,Long>, JpaSpecificationExecutor<AcQuiz> {

    Optional<AcQuiz> findById(Integer id);
}
