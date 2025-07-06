package com.example.chat.application.facade;

import com.example.chat.dto.message.MessageProcessingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventPublishingComponent {
    private final ApplicationEventPublisher eventPublisher;

    public Mono<MessageProcessingContext> publishEvent(MessageProcessingContext context) {
        // Mono.fromRunnable을 사용해 동기적인 이벤트 발행 작업을 리액티브 체인에 통합
        return Mono.fromRunnable(() -> {
            MessageSentEvent event = new MessageSentEvent(this, context.getSavedMessage());
            eventPublisher.publishEvent(event);
        }).thenReturn(context); // 작업 완료 후 context를 그대로 반환
    }
}
