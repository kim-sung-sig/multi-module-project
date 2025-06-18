package com.example.chat.core.domain.repository.message.impl;

import com.example.chat.core.domain.model.message.MessageType;
import com.example.chat.core.domain.repository.message.MessageWriter;
import com.example.chat.core.infra.entity.message.MessageEntity;
import com.example.chat.core.infra.entity.message.TextMessageEntity;
import com.example.chat.core.infra.kafka.ChatEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageWriterImpl implements MessageWriter {
//    private final TextMessageJpaRepository textMessageJpaRepository;
    private final ChatEventPublisher eventPublisher;

    @Override
    public void write(MessageEntity message) {

        MessageType messageType = message.getType();

        // TODO TODO TODO TODO TODO
        // 1. 메시지를 RDB에 저장
        if (messageType == MessageType.TEXT) {
            TextMessageEntity textMessageEntity = (TextMessageEntity) message;
//            textMessageJpaRepository.save(textMessageEntity);
        }
        // 2. 메시지 생성 이벤트를 발행
        eventPublisher.publishMessageCreated(message);
    }
}
