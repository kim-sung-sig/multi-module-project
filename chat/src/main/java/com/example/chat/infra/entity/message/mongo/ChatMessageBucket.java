package com.example.chat.infra.entity.message.mongo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "chat_messages")
@CompoundIndex(name = "roomId_bucketNumber_idx", def = "{'roomId': 1, 'bucketNumber': -1}")
public class ChatMessageBucket {

    @Id
    private String id;

    private Long roomId; // 어떤 채팅방의 버킷인지 식별

    private int bucketNumber; // 버킷의 순서 (0, 1, 2, ...)

    private int messageCount; // 현재 버킷에 저장된 메시지 수

    private LocalDateTime createdAt; // 버킷 생성 시간

    private List<MessagePayload> messages; // 실제 메시지 데이터 배열

    @Data
    @Builder
    public static class MessagePayload {
        private Long messageId;
        private Long senderId;
        private String senderName;
        private String type;
        private String details;
        private LocalDateTime sentAt;
    }

}
