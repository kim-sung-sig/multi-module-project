package com.example.chat.core.domain.repository.room;

import com.example.chat.core.infra.entity.room.ChatRoomEntity;

public interface ChatRoomReader {
    ChatRoomEntity read(Long chatRoomId);
}
