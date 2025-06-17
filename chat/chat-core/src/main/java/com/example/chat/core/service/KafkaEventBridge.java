package com.example.chat.core.service;

import com.example.chat.core.event.MessageCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventBridge {

    private static final String TOPIC_CHAT_MESSAGES = "chat-messages";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // @EventListener: Spring 이벤트를 수신합니다.
    // @TransactionalEventListener: 트랜잭션이 성공적으로 커밋된 후에만 이벤트를 처리합니다.
    @TransactionalEventListener
    public void handleMessageCreatedEvent(MessageCreatedEvent event) {
        var message = event.getMessageEntity();
        log.info("Received Spring event, publishing to Kafka. Message ID: {}", message.getId());

        try {
            // Key: chatRoomId, Value: Message 객체 (JSON 직렬화)
            kafkaTemplate.send(TOPIC_CHAT_MESSAGES, message.getChatRoomId().toString(), message);
        } catch (Exception e) {
            log.error("Failed to publish message to Kafka. Message ID: {}", message.getId(), e);
            // TODO: 예외 처리 및 DLQ(Dead Letter Queue) 전송 로직
        }
    }
}
