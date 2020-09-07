package com.soaint.repository;


import com.soaint.entity.AcClientsPrivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AcClientsPrivateRepository extends JpaRepository<AcClientsPrivate,Long>, JpaSpecificationExecutor<AcClientsPrivate> {

    Optional<AcClientsPrivate> findByEmail(String email);
    boolean  existsByEmail(String email);

    boolean existsById(Integer id);
    Optional<AcClientsPrivate> findById(Integer id);

    void deleteById(Integer id);

    @Query(value = "SELECT COUNT(DISTINCT email) FROM ac_clients_private", nativeQuery = true)
    Long ClientsPrivateCount();

    @Query(value="select COUNT(DISTINCT email) from ac_clients_private where created_at >=current_date -7", nativeQuery = true)
    Long ClientsPrivateCountLastWeek();

    @Query(value="select COUNT(DISTINCT email) from ac_clients_private where created_at >=current_date -0", nativeQuery = true)
    Long ClientsPrivateCountLastDay();

    @Query(value = "select * from ac_clients_private where \n" +
            "           id in (Select max(id) FROM ac_clients_private group by email)", nativeQuery = true)
    List<AcClientsPrivate> ClientsPrivate();


}
