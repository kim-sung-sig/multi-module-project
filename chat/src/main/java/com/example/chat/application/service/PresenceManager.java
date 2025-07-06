package com.example.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PresenceManager {

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final String PRESENCE_KEY_PREFIX = "presence:chatroom:";

    private @NonNull String getKey(Long chatRoomId) {
        return PRESENCE_KEY_PREFIX + chatRoomId;
    }

    /**
     * 사용자 입장
     * @param chatRoomId 채팅방 id
     * @param userId 참가할 유저 id
     * @return 입장 성공 여부
     */
    public Mono<Long> userEntered(Long chatRoomId, Long userId) {
        return redisTemplate.opsForSet().add(getKey(chatRoomId), String.valueOf(userId));
    }

    /**
     * 사용자 퇴장
     * @param chatRoomId 채팅방 id
     * @param userId 퇴장할 유저 id
     * @return 퇴장 성공 여부
     */
    public Mono<Long> userLeft(Long chatRoomId, Long userId) {
        return redisTemplate.opsForSet().remove(getKey(chatRoomId), String.valueOf(userId));
    }

    /**
     * 채팅방 사용자 목록 조회
     * @param chatRoomId 채팅방 id
     * @return 채팅방에 참여중인 유저 ids
     */
    public Flux<Long> findPresentUsers(Long chatRoomId) {
        return redisTemplate.opsForSet().members(getKey(chatRoomId)).map(Long::valueOf);
    }

}
