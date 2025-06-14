package com.example.chat.core.infra.persistence.room;

import com.example.chat.core.domain.model.log.ChatRoomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomLogJpaRepository extends JpaRepository<ChatRoomLog, Long> {
}
