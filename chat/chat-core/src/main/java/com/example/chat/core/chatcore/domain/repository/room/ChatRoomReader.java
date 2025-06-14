package com.example.chat.core.chatcore.domain.repository.room;

import com.example.chat.core.chatcore.infra.entity.room.ChatRoomEntity;

public interface ChatRoomReader {
    ChatRoomEntity read(Long chatRoomId);
}
