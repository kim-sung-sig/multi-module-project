package com.example.chat.core.chatcore.domain.repository.room;

public interface PresenceWriter {
    void write(Long chatRoomId, Long userId);
    void delete(Long chatRoomId, Long userId);
}
