package com.example.chat.dto.room;

public record UserChatRoomKey (Long chatRoomId, Long userId) {

    public UserChatRoomKey {
        if (chatRoomId == null || userId == null) {
            throw new IllegalArgumentException("chatRoomId and userId cannot be null");
        }
    }

}
