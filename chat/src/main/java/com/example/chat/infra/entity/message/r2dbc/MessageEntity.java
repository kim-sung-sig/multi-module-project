package com.example.chat.infra.entity.message.r2dbc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("message") // 테이블명은 단수형을 권장합니다. (dn_message -> message)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("type")
    @Builder.Default
    private MessageType type = MessageType.TEXT; // 기본값 설정

    @Column("room_id")
    private Long roomId;

    @Column("sender_id")
    private Long senderId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    /**
     * 메시지 타입별 상세 정보를 JSON 문자열로 저장
     * 예: {"content": "안녕하세요"}
     */
    @Column("details")
    private String details;

    public enum MessageType {
        TEXT,
        IMAGE,
        AUDIO,
        VIDEO
    }

    public record TextMessageDetails(String content) {}
}
