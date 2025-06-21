package com.example.chat.core.domain.model.message;

import com.example.chat.core.infra.entity.message.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Message {

    protected Long id;
    protected Long roomId;
    protected Long senderId;
    protected LocalDateTime createdAt;

    public Message(Long id, Long roomId, Long senderId) {
        this.id = id;
        this.roomId = roomId;
        this.senderId = senderId;
        this.createdAt = LocalDateTime.now();
    }

    public abstract MessageEntity toMessageEntity();

}
