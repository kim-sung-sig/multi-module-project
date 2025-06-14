package com.example.chat.core.chatcore.infra.persistence.room;

import com.example.chat.core.chatcore.domain.model.log.ChatRoomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomLogJpaRepository extends JpaRepository<ChatRoomLog, Long> {
}
