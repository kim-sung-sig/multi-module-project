package com.example.chat.core.chatcore.infra.persistence.room;

import com.example.chat.core.chatcore.infra.entity.room.UserChatRoomEntity;
import com.example.chat.core.chatcore.infra.entity.room.UserChatRoomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomJpaRepository extends JpaRepository<UserChatRoomEntity, UserChatRoomId> {
}
