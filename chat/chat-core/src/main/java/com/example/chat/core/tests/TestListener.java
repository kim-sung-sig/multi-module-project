package com.example.chat.core.tests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestListener {

    private final TestMongoRepository testMongoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {

        TestDocument doc = new TestDocument();
        doc.setId(UUID.randomUUID().toString());
        doc.setSender("test");
        doc.setCreatedAt(LocalDateTime.now());
        kafkaTemplate.send("test-topic", doc);
    }

    /**
     * [디버깅을 위한 수정]
     * 1. 리스너 파라미터를 String으로 변경하여 역직렬화 과정 없이 원본 메시지를 받습니다.
     * 2. 수신된 원본 JSON 문자열을 로그로 출력하여 메시지 수신 자체는 성공했는지 확인합니다.
     * 3. 수동으로 JSON 문자열을 객체 리스트로 변환해보고, 이 과정에서 발생하는 예외를 직접 확인합니다.
     */
    @KafkaListener(topics = "test-topic", groupId = "test-group", containerFactory = "kafkaListenerContainerFactory")
    public CompletableFuture<Void> listen(TestDocument testDocument) {
        log.info("======================================================");
        log.info("Successfully received raw JSON message from Kafka: {}", testDocument);
        log.info("======================================================");

        return CompletableFuture.runAsync(() -> System.out.println(
                "Received message: " + testDocument.toString()
        ));
    }


}
