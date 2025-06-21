package com.example.chat.core.domain.model.message;

import com.example.chat.core.infra.entity.message.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TextMessage extends Message {

    private String content;

    public TextMessage(Long id, Long roomId, Long senderId, String content) {
        super(id, roomId, senderId);
        this.content = content;
    }

    public MessageEntity toMessageEntity() {
        return new MessageEntity();
    }
}
