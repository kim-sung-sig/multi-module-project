package com.example.chat.core.domain.repository.room.impl;

import com.example.chat.core.domain.repository.room.ChatRoomReader;
import com.example.chat.core.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.infra.persistence.room.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomReaderImpl implements ChatRoomReader {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    @Override
    public ChatRoomEntity read(Long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
    }
}
