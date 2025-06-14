package com.example.chat.core.chatcore.domain.model.log;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_room_log")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatRoomId;
    private Long userId;

    @Convert(converter = ChatRoomLogTypeConverter.class)
    private ChatRoomLogType type;

    @CreatedDate
    private LocalDateTime timestamp;

    public ChatRoomLog(Long chatRoomId, Long userId, ChatRoomLogType type) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
}
