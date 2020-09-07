package com.soaint.repository;

import com.soaint.entity.AcClientsPrivate;
import com.soaint.entity.AcDataClientsPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface AcDataClientsPrivateRepository extends JpaRepository<AcDataClientsPrivate,Long>, JpaSpecificationExecutor<AcDataClientsPrivate> {

    boolean existsById(Integer id);

    Optional<AcDataClientsPrivate> findById(Integer id);

    void deleteById(Integer id);
}
