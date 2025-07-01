package com.example.chat.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    /**
     * 채팅방에 입장 (실시간 접속 정보 및 로그 기록)
     */
    @Transactional
    public void enterRoom(Long chatRoomId, Long userId) {
    }

    /**
     * 채팅방에서 퇴장 (실시간 접속 정보 및 로그 기록)
     */
    @Transactional
    public void leaveRoom(Long chatRoomId, Long userId) {
    }

    /**
     * 채팅방에 가입 (영속적인 관계 설정)
     */
    @Transactional
    public void joinRoom(Long chatRoomId, Long userId) {
    }
}
