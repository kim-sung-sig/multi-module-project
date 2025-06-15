package com.example.chat.core.tests;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestMongoRepository testMongoRepository;

    @PostConstruct
    public void init() {
//        TestDocument testDocument = new TestDocument();
//        testDocument.setSender("11");
//        testDocument.setContent("asfasf");
//        testMongoRepository.save(testDocument);
//
//
//        List<TestDocument> testDocuments = testMongoRepository.findAll();
//        System.out.println(testDocuments);
    }
}
