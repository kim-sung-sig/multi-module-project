package com.example.chat.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.chat")
public class ChatApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApiApplication.class, args);
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

//    @Autowired
//    private TestMongoRepository testMongoRepository;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

    @PostConstruct
    public void init() {
//        TestDocument testDocument = new TestDocument();
//        testDocument.setSender("11");
//        testDocument.setContent("asfasf");
//        testMongoRepository.save(testDocument);


//        List<TestDocument> testDocuments = testMongoRepository.findAll();
//        System.out.println(testDocuments);
//
//        kafkaTemplate.send("test-topic", "test-key", testDocuments);
    }

//    @KafkaListener(topics = "test-topic", groupId = "test-group")
//    public void listen(List<TestDocument> messages) {
//        System.out.println("Received messages: " + messages);
//        // 메시지 처리 로직 추가
//    }
}
