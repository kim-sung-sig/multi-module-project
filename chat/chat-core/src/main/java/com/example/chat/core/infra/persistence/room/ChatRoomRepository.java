package com.example.chat.core.infra.persistence.room;

import com.example.chat.core.infra.entity.room.ChatRoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends R2dbcRepository<ChatRoomEntity, Long> {
}
