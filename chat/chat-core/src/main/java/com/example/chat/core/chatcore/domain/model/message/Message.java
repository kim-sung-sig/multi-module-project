package com.example.chat.core.chatcore.domain.model.message;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Message {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Column(name = "sender_id")
    private Long senderId;

    @CreatedDate
    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    public Message(Long id, Long chatRoomId, Long senderId) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.sentAt = LocalDateTime.now();
    }

    public abstract MessageType getType();
    public abstract String getPreviewText();
}
