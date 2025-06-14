package com.example.chat.core.domain.repository.message.impl;

import com.example.chat.core.domain.repository.message.MessageWriter;
import com.example.chat.core.infra.entity.message.MessageEntity;
import com.example.chat.core.infra.kafka.ChatEventPublisher;
import com.example.chat.core.infra.persistence.message.MessageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageWriterImpl implements MessageWriter {
    private final MessageJpaRepository messageJpaRepository;
    private final ChatEventPublisher eventPublisher;

    @Override
    public void write(MessageEntity message) {
        // 1. 메시지를 RDB에 저장
        messageJpaRepository.save(message);
        // 2. 메시지 생성 이벤트를 발행
        eventPublisher.publishMessageCreated(message);
    }
}
