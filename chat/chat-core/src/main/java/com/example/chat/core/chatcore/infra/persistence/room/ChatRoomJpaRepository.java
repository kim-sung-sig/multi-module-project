package com.example.chat.core.chatcore.infra.persistence.room;

import com.example.chat.core.chatcore.infra.entity.room.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
}
