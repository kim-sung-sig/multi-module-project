package com.example.chat.core.infra.kafka;

import com.example.chat.core.infra.entity.message.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatEventPublisher {
    private static final String TOPIC_CHAT_MESSAGES = "chat-messages";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMessageCreated(MessageEntity messageEntity) {
        kafkaTemplate.send(TOPIC_CHAT_MESSAGES, messageEntity.getChatRoomId().toString(), messageEntity);
    }

}
