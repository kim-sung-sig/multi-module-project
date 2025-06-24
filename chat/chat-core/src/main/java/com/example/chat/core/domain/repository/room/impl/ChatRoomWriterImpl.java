package com.example.chat.core.domain.repository.room.impl;

import com.example.chat.core.domain.repository.room.ChatRoomWriter;
import com.example.chat.core.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.infra.entity.room.UserChatRoomEntity;
import com.example.chat.core.infra.persistence.room.ChatRoomRepository;
import com.example.chat.core.infra.persistence.room.UserChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomWriterImpl implements ChatRoomWriter {
    private final ChatRoomRepository chatRoomRepository;
    private final UserChatRoomJpaRepository userChatRoomJpaRepository;
    @Override
    public void write(ChatRoomEntity chatRoom) {
        chatRoomRepository.save(chatRoom);
    }
    @Override
    public void join(UserChatRoomEntity userChatRoom) {
        userChatRoomJpaRepository.save(userChatRoom);
    }
}
