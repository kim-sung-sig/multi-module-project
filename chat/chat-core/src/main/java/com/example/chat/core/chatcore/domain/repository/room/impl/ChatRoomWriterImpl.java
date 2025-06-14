package com.example.chat.core.chatcore.domain.repository.room.impl;

import com.example.chat.core.chatcore.domain.repository.room.ChatRoomWriter;
import com.example.chat.core.chatcore.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.chatcore.infra.entity.room.UserChatRoomEntity;
import com.example.chat.core.chatcore.infra.persistence.room.ChatRoomJpaRepository;
import com.example.chat.core.chatcore.infra.persistence.room.UserChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomWriterImpl implements ChatRoomWriter {
    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private final UserChatRoomJpaRepository userChatRoomJpaRepository;
    @Override
    public void write(ChatRoomEntity chatRoom) {
        chatRoomJpaRepository.save(chatRoom);
    }
    @Override
    public void join(UserChatRoomEntity userChatRoom) {
        userChatRoomJpaRepository.save(userChatRoom);
    }
}
