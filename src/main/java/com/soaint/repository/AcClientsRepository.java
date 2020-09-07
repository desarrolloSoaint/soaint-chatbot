package com.soaint.repository;


import com.soaint.entity.AcClients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AcClientsRepository extends JpaRepository<AcClients,Long>, JpaSpecificationExecutor<AcClients> {
    Optional<AcClients> findByEmail(String email);
    boolean  existsByEmail(String email);
    boolean existsById(Integer id);
    Optional<AcClients> findById(Integer id);
    void deleteById(Integer id);


    @Query(value = "select * from ac_clients where\n" +
            "id in (Select max(id) FROM ac_clients group by email)", nativeQuery = true)
    List<AcClients> ClientsPublic();

    @Query(value = "SELECT COUNT(DISTINCT email) FROM ac_clients", nativeQuery = true)
    Long ClientsPublicCount();

    @Query(value="select COUNT(DISTINCT email) from ac_clients where created_at >=current_date -7", nativeQuery = true)
    Long ClientsPublicCountLastWeek();

    @Query(value="select COUNT( DISTINCT email) from ac_clients where created_at >=current_date -0", nativeQuery = true)
    Long ClientsPublicCountLastDay();



}
