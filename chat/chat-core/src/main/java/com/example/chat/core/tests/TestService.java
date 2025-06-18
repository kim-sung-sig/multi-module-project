package com.example.chat.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(List<TestDocument> messages) {
        System.out.println("Received messages: " + messages);
        // 메시지 처리 로직 추가
    }

}
