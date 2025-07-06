package com.example.chat.infra.persistence.message.mongo;

import com.example.chat.infra.entity.message.mongo.ChatMessageBucket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatMessageBucketRepository extends ReactiveMongoRepository<ChatMessageBucket, String> {

    Mono<ChatMessageBucket> findFirstByRoomIdOrderByBucketNumberDesc(Long roomId);

    Flux<ChatMessageBucket> findByRoomIdOrderByBucketNumberDesc(Long roomId, Pageable pageable);

}
