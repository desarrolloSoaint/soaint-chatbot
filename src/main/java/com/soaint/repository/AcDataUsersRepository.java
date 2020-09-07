package com.soaint.repository;

import com.soaint.entity.AcDataUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AcDataUsersRepository extends JpaRepository<AcDataUsers, Integer>, JpaSpecificationExecutor<AcDataUsers> {

}
