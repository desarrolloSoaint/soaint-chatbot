package com.soaint.repository;

import com.soaint.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>, JpaSpecificationExecutor<Rol> {
      Optional<Rol> findByRolName(String rolname);
      boolean existsByRolName(String rolname);
}
