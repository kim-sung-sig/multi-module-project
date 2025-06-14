package com.example.chat.core.infra.entity.message;

import com.example.chat.core.domain.model.message.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "text_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextMessageEntity extends MessageEntity {

    private String content;

    public TextMessageEntity(Long id, Long chatRoomId, Long senderId, String content) {
        super(id, chatRoomId, senderId);
        this.content = content;
    }

    @Override
    public MessageType getType() {
        return MessageType.TEXT;
    }

    @Override
    public String getPreviewText() {
        return this.content;
    }
}
