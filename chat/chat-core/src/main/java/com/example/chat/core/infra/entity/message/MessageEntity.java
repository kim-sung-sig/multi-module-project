package com.example.chat.core.infra.entity.message;

import com.example.chat.core.domain.model.message.MessageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class MessageEntity {

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

    public MessageEntity(Long id, Long chatRoomId, Long senderId) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.sentAt = LocalDateTime.now();
    }

    public abstract MessageType getType();
    public abstract String getPreviewText();
}
