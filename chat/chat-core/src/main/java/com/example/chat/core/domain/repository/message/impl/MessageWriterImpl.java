package com.example.chat.core.domain.repository.message.impl;

import com.example.chat.core.domain.model.message.MessageType;
import com.example.chat.core.domain.model.message.TextMessage;
import com.example.chat.core.domain.repository.message.MessageWriter;
import com.example.chat.core.infra.entity.message.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageWriterImpl implements MessageWriter {

    @Override
    public void write(MessageEntity messageEntity) {

        MessageType messageType = messageEntity.getType();

        // 1. 메시지를 RDB에 저장
        if (messageType == MessageType.TEXT) {
            TextMessage textMessage = (TextMessage) messageEntity;
//            textMessageJpaRepository.save(textMessageEntity);
        }
    }
}
