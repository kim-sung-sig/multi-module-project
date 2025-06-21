package com.example.chat.core.tests;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TestDocument produce(TestDocument testDocument) {
        final String topic = "test-topic";

        kafkaTemplate.send(topic, testDocument);

        return testDocument;
    }
}
