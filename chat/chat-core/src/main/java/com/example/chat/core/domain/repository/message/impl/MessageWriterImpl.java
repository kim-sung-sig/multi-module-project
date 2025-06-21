package com.example.chat.core.domain.repository.message.impl;

import com.example.chat.core.domain.model.message.MessageType;
import com.example.chat.core.domain.model.message.TextMessage;
import com.example.chat.core.domain.repository.message.MessageWriter;
import com.example.chat.core.infra.entity.message.MessageEntity;
import com.example.chat.core.infra.kafka.ChatEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageWriterImpl implements MessageWriter {

    private final ChatEventPublisher eventPublisher;

    @Override
    public void write(MessageEntity messageEntity) {

        MessageType messageType = messageEntity.getType();

        // TODO TODO TODO TODO TODO
        // 1. 메시지를 RDB에 저장
        if (messageType == MessageType.TEXT) {
            TextMessage textMessage = (TextMessage) messageEntity;
//            textMessageJpaRepository.save(textMessageEntity);
        }
        // 2. 메시지 생성 이벤트를 발행
        eventPublisher.publishMessageCreated(messageEntity);
    }
}
