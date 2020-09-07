package com.soaint.repository;

import com.soaint.entity.CbAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CbAvatarRepository extends JpaRepository<CbAvatar, Long>, JpaSpecificationExecutor<CbAvatar> {
    Optional<CbAvatar> findByAvatar(String avatar);
    boolean existsByAvatar(String avatar);

}
