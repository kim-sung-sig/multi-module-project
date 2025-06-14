package com.example.chat.core.chatcore.infra.entity.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class MessageReadId implements Serializable {
    private Long messageId;
    private Long userId;
}
