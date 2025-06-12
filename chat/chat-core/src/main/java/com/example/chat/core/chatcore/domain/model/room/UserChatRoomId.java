package com.example.chat.core.chatcore.domain.model.room;

import java.io.Serializable;

import lombok.Data;

@Data
public class UserChatRoomId implements Serializable {
    private Long chatRoomId;
    private Long userId;
}
