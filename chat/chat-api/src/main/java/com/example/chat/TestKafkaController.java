package com.example.chat;

import com.example.chat.core.tests.TestDocument;
import com.example.chat.core.tests.TestEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestKafkaController {

    private static final String TEST_TOPIC = "test-topic";
    private final TestEventProducer testEventProducer;

    @GetMapping("/kafka/send")
    public Mono<ResponseEntity<String>> sendMessage() {
        // 리스너가 기대하는 타입인 List<TestDocument> 형태의 페이로드를 생성합니다.
        TestDocument messagePayload = new TestDocument(
                "id-" + System.currentTimeMillis(),
                "Message from WebFlux Controller",
                "asfasfasf",
                "asfasf"
        );

        log.info("Attempting to send message via GET request: {}", messagePayload);

        // kafkaTemplate.send는 비동기적으로 동작하며 CompletableFuture를 반환합니다.
        // 이를 Mono로 변환하여 WebFlux 스트림에 통합합니다.
        testEventProducer.produce(messagePayload);
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send message:x" )
        );
    }
}
