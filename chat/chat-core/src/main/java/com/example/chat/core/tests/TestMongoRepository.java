package com.example.chat.core.tests;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestMongoRepository extends MongoRepository<TestDocument, String> {
}
