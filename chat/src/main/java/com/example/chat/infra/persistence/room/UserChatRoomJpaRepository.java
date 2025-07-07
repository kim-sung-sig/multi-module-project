package com.example.chat.infra.persistence.room;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.example.chat.infra.entity.room.UserChatRoomEntity;

@Repository
public interface UserChatRoomJpaRepository extends R2dbcRepository<UserChatRoomEntity, Long> {
}
