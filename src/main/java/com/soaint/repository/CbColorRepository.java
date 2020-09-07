package com.soaint.repository;

import com.soaint.entity.CbColor;
import com.soaint.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CbColorRepository extends JpaRepository<CbColor,Long>, JpaSpecificationExecutor<CbColor> {
}
