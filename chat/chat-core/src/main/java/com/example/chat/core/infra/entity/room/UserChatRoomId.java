package com.example.chat.core.infra.entity.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserChatRoomId implements Serializable {
    private Long chatRoomId;
    private Long userId;
}
