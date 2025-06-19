package com.example.chat.core.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestListener {

    private final TestMongoRepository testMongoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // ApplicationReadyEvent: 애플리케이션이 완전히 준비된 후에 이벤트를 발행합니다.
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) throws JsonProcessingException {
        List<TestDocument> testDocuments = testMongoRepository.findAll();
        log.info("Test documents found: {}", testDocuments);

        if (!testDocuments.isEmpty()) {
            log.info("Sending test documents to Kafka topic 'test-topic'");
            kafkaTemplate.send("test-topic", testDocuments.get(0));
        }
    }

    /**
     * [디버깅을 위한 수정]
     * 1. 리스너 파라미터를 String으로 변경하여 역직렬화 과정 없이 원본 메시지를 받습니다.
     * 2. 수신된 원본 JSON 문자열을 로그로 출력하여 메시지 수신 자체는 성공했는지 확인합니다.
     * 3. 수동으로 JSON 문자열을 객체 리스트로 변환해보고, 이 과정에서 발생하는 예외를 직접 확인합니다.
     */
    @KafkaListener(topics = "test-topic", groupId = "test-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(TestDocument testDoc) {
        log.info("======================================================");
        log.info("STEP 1: Successfully received raw JSON message from Kafka: {}", testDoc);
        log.info("======================================================");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // 수동으로 JSON을 List<TestDocument> 타입으로 변환 시도

            log.info("STEP 2: Successfully deserialized messages manually: {}", testDoc);

            // 원래의 메시지 처리 로직 추가
        } catch (Exception e) {
            // 만약 여기서 에러가 발생한다면, TestDocument 클래스의 구조나 데이터에 문제가 있는 것입니다.
            log.error("STEP 2 FAILED: Error during manual deserialization!", e);
        }
    }


}
