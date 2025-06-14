package com.example.chat.core.chatcore.domain.repository.room;

import java.util.Set;

public interface PresenceReader {
    Set<Long> getUsers(Long chatRoomId);
}
