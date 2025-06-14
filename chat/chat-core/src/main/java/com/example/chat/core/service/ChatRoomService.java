package com.example.chat.core.service;

import com.example.chat.core.domain.repository.room.ChatRoomReader;
import com.example.chat.core.domain.repository.room.ChatRoomWriter;
import com.example.chat.core.domain.repository.room.PresenceWriter;
import com.example.chat.core.infra.entity.room.ChatRoomEntity;
import com.example.chat.core.infra.entity.room.UserChatRoomEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final PresenceWriter presenceWriter;
    private final ChatRoomWriter chatRoomWriter;
    private final ChatRoomReader chatRoomReader;

    /**
     * 채팅방에 입장 (실시간 접속 정보 및 로그 기록)
     */
    @Transactional
    public void enterRoom(Long chatRoomId, Long userId) {
        // PresenceWriter가 Redis와 RDB에 모두 기록합니다.
        presenceWriter.write(chatRoomId, userId);
    }

    /**
     * 채팅방에서 퇴장 (실시간 접속 정보 및 로그 기록)
     */
    @Transactional
    public void leaveRoom(Long chatRoomId, Long userId) {
        presenceWriter.delete(chatRoomId, userId);
    }

    /**
     * 채팅방에 가입 (영속적인 관계 설정)
     */
    @Transactional
    public void joinRoom(Long chatRoomId, Long userId) {
        ChatRoomEntity chatRoom = chatRoomReader.read(chatRoomId);
        UserChatRoomEntity userChatRoom = new UserChatRoomEntity(chatRoom.getId(), userId);
        chatRoomWriter.join(userChatRoom);
    }
}
