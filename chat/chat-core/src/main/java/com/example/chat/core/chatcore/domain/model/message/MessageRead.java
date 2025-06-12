package com.example.chat.core.chatcore.domain.model.message;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 어떤 사용자가 어떤 메시지를 읽었는지 기록하는 엔티티입니다.
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "message_read")
@IdClass(MessageReadId.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageRead {

    @Id
    @Column(name = "message_id")
    private Long messageId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @CreatedDate
    @Column(name = "read_at")
    private LocalDateTime readAt;

    public MessageRead(Long messageId, Long userId) {
        this.messageId = messageId;
        this.userId = userId;
        this.readAt = LocalDateTime.now();
    }
}
