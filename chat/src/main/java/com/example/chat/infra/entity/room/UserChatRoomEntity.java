package com.example.chat.infra.entity.room;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "user_chat_room")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChatRoomEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("chat_room_id")
    private Long chatRoomId;

    @Column("user_id")
    private Long userId;

    @Setter
    @Column("room_name")
    private String roomName;

    @Column("joined_at")
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
