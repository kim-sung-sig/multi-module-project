package com.example.chat.core.chatcore.infra.entity.room;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserChatRoomId.class)
public class UserChatRoomEntity {

    @Id
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Setter
    @Column(nullable = false)
    private String roomName;

    @CreatedDate
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    public UserChatRoomEntity(Long chatRoomId, Long userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.joinedAt = LocalDateTime.now();
    }

    public UserChatRoomEntity(Long chatRoomId, Long userId, String roomName) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.roomName = roomName;
        this.joinedAt = LocalDateTime.now();
    }

}
