package com.example.chat.dto.message;

import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.Data;

@Data
public class MessageSendRequest {

    private Long chatRoomId;

    private MessageEntity.MessageType messageType;

    private Long senderId;

}
