package com.example.chat.infra.persistence.room;

import com.example.chat.dto.room.UserChatRoomKey;
import com.example.chat.infra.entity.room.UserChatRoomEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomJpaRepository extends R2dbcRepository<UserChatRoomEntity, UserChatRoomKey> {
}
