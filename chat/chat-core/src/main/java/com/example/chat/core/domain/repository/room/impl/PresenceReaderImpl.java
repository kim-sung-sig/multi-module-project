package com.example.chat.core.domain.repository.room.impl;

import com.example.chat.core.domain.repository.room.PresenceReader;
import com.example.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PresenceReaderImpl implements PresenceReader {
    private final StringRedisTemplate redisTemplate;

    private String getKey(Long chatRoomId) { return "presence:chatroom:" + chatRoomId; }

    @Override
    public Set<Long> getUsers(Long chatRoomId) {
        Set<String> userIdStrs = redisTemplate.opsForSet().members(getKey(chatRoomId));
        return CommonUtil.isEmpty(userIdStrs)
                ? Set.of() // 가변, 불변 필요
                : userIdStrs.stream().map(Long::valueOf).collect(Collectors.toSet());
    }
}
