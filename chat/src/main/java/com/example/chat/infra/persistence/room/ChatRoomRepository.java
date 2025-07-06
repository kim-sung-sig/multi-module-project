package com.example.chat.infra.persistence.room;

import com.example.chat.infra.entity.room.ChatRoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends R2dbcRepository<ChatRoomEntity, Long> {
}
