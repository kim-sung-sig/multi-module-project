package com.example.chat.core.chatcore.domain.model.room;

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
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserChatRoomId.class)
public class UserChatRoom {

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

    public UserChatRoom(Long chatRoomId, Long userId) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.joinedAt = LocalDateTime.now();
    }

    public UserChatRoom(Long chatRoomId, Long userId, String roomName) {
        this.chatRoomId = chatRoomId;
        this.userId = userId;
        this.roomName = roomName;
        this.joinedAt = LocalDateTime.now();
    }

}
