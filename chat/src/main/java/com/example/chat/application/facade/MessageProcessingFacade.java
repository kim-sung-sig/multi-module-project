package com.example.chat.application.facade;

import com.example.chat.application.service.message.ValidationComponent;
import com.example.chat.dto.message.MessageProcessingContext;
import com.example.chat.dto.message.MessageSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MessageProcessingFacade {

    private final ValidationComponent validationComponent;
    private final PersistenceComponent persistenceComponent;
    private final EventPublishingComponent eventPublishingComponent;

    public Mono<Void> process(MessageSendRequest request) {
        // 1. 초기 컨텍스트 생성
        MessageProcessingContext initialContext = new MessageProcessingContext(request);

        // 2. 각 컴포넌트를 순차적으로 실행하는 리액티브 파이프라인 조립
        return Mono.just(initialContext)
                .flatMap(validationComponent::validate)
                .flatMap(persistenceComponent::processAndSave)
                .flatMap(eventPublishingComponent::publishEvent)
                .then(); // 모든 작업이 끝나면 Mono<Void>로 변환하여 완료 신호 전송
    }
}
