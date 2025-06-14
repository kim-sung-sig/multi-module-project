package com.example.chat.core.chatcore.domain.model.message;

import com.example.common.util.UuidUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

// aggreroot
@Getter
public class ChatMessage {
    private final UUID id;
    private final String roomId;
    private final String senderId;
    private final String content;
    private final MessageType type;
    private final LocalDateTime createdAt;
    private final MessageStatus status;

    private ChatMessage(Builder builder) {
        validateMessage(builder);  // 메시지 검증
        this.id = builder.id;
        this.roomId = builder.roomId;
        this.senderId = builder.senderId;
        this.content = builder.content;
        this.type = builder.type;
        this.createdAt = builder.createdAt;
        this.status = builder.status;
    }

    private void validateMessage(Builder builder) {
        if (builder.content == null || builder.content.trim().isEmpty()) {
            throw new IllegalArgumentException("메시지 내용은 비어있을 수 없습니다.");
        }
        if (builder.content.length() > 1000) {
            throw new IllegalArgumentException("메시지는 1000자를 초과할 수 없습니다.");
        }
        if (builder.roomId == null || builder.roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("채팅방 ID는 필수입니다.");
        }
        if (builder.senderId == null || builder.senderId.trim().isEmpty()) {
            throw new IllegalArgumentException("발신자 ID는 필수입니다.");
        }
    }

    public static ChatMessage create(String roomId, String senderId, String content, MessageType type) {
        return new Builder()
            .id(UuidUtil.generate())
            .roomId(roomId)
            .senderId(senderId)
            .content(content)
            .type(type)
            .createdAt(LocalDateTime.now())
            .status(MessageStatus.SENT)
            .build();
    }

    public static class Builder {
        private UUID id;
        private String roomId;
        private String senderId;
        private String content;
        private MessageType type;
        private LocalDateTime createdAt;
        private MessageStatus status;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder roomId(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Builder senderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder type(MessageType type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder status(MessageStatus status) {
            this.status = status;
            return this;
        }

        public ChatMessage build() {
            return new ChatMessage(this);
        }
    }
}
