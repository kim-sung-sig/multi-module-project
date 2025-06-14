package com.example.chat.core.domain.repository.room;

import com.example.chat.core.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.infra.entity.room.UserChatRoomEntity;

public interface ChatRoomWriter {
    void write(ChatRoomEntity chatRoom);
    void join(UserChatRoomEntity userChatRoom);
}
