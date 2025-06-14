package com.example.chat.core.domain.repository.room;

import java.util.Set;

public interface PresenceReader {
    Set<Long> getUsers(Long chatRoomId);
}
