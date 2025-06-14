package com.example.chat.core.domain.repository.room.impl;

import com.example.chat.core.domain.model.log.ChatRoomLog;
import com.example.chat.core.domain.model.log.ChatRoomLogType;
import com.example.chat.core.domain.repository.room.PresenceWriter;
import com.example.chat.core.infra.persistence.room.ChatRoomLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PresenceWriterImpl implements PresenceWriter {
    private final StringRedisTemplate redisTemplate;
    private final ChatRoomLogJpaRepository logRepository; // 입장/퇴장 기록용

    private String getKey(Long chatRoomId) { return "presence:chatroom:" + chatRoomId; }

    @Override
    public void write(Long chatRoomId, Long userId) {
        // 1. Redis에 현재 접속자 정보 저장 (실시간 확인용)
        redisTemplate.opsForSet().add(getKey(chatRoomId), String.valueOf(userId));
        // 2. RDB에 입장 로그 기록 (영구 보관용)
        logRepository.save(new ChatRoomLog(chatRoomId, userId, ChatRoomLogType.ENTER));
    }

    @Override
    public void delete(Long chatRoomId, Long userId) {
        redisTemplate.opsForSet().remove(getKey(chatRoomId), String.valueOf(userId));
        logRepository.save(new ChatRoomLog(chatRoomId, userId, ChatRoomLogType.LEAVE));
    }
}
