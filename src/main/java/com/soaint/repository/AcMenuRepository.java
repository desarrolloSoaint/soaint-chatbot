package com.soaint.repository;

import com.soaint.entity.AcMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AcMenuRepository extends JpaRepository<AcMenu, Long>, JpaSpecificationExecutor<AcMenu> {

}

