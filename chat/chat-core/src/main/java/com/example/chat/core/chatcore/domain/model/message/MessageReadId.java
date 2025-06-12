package com.example.chat.core.chatcore.domain.model.message;

import java.io.Serializable;

import lombok.Data;

@Data
public class MessageReadId implements Serializable {
    private Long messageId;
    private Long userId;
}
