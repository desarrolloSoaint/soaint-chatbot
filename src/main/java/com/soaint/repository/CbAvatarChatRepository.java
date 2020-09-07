package com.soaint.repository;

import com.soaint.entity.CbAvatarChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CbAvatarChatRepository extends JpaRepository<CbAvatarChat, Long>, JpaSpecificationExecutor<CbAvatarChat> {
}
