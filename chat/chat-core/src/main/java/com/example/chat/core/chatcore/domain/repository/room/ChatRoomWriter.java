package com.example.chat.core.chatcore.domain.repository.room;

import com.example.chat.core.chatcore.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.chatcore.infra.entity.room.UserChatRoomEntity;

public interface ChatRoomWriter {
    void write(ChatRoomEntity chatRoom);
    void join(UserChatRoomEntity userChatRoom);
}
