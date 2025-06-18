package com.example.chat.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestListener {

    private final TestMongoRepository testMongoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {

        List<TestDocument> testDocuments = testMongoRepository.findAll();
        System.out.println(testDocuments);

        kafkaTemplate.send("test-topic", "test-key", testDocuments);
    }
}
