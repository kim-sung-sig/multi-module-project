package com.example.chat.core.chatcore.domain.model.message;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "text_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TextMessage extends Message {

    private String content;

    public TextMessage(Long id, Long chatRoomId, Long senderId, String content) {
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
