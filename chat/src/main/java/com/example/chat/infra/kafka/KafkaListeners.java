package com.example.chat.infra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListeners {

    /**
     * Consumer 1: 메시지를 Read DB (MongoDB)로 동기화하는 역할
     */
    @KafkaListener(topics = "chat-messages", groupId = "chat-sync-group")
    public void syncMessageToReadDb(String messageJson) {
        log.info("[Sync Group] Consumed message for DB sync: {}", messageJson);
        // TODO: MongoDB에 저장하는 로직
    }

    /**
     * Consumer 2: 실시간 알림을 보내는 역할
     */
    @KafkaListener(topics = "chat-messages", groupId = "realtime-push-group")
    public void pushNotification(String messageJson) {
        log.info("[Push Group] Consumed message for push notification: {}", messageJson);
        // TODO: WebSocket 등으로 클라이언트에 알림을 보내는 로직
    }

    /**
     * Consumer 3: 통계 데이터를 수집하는 역할
     */
    @KafkaListener(topics = "chat-messages", groupId = "statistics-group")
    public void aggregateStatistics(String messageJson) {
        log.info("[Statistics Group] Consumed message for statistics: {}", messageJson);
        // TODO: 통계 데이터를 집계하는 로직
    }
}
