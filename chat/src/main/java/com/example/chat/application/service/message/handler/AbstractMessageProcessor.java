package com.example.chat.application.service.message.handler;

import com.example.chat.dto.message.MessageCreatedEvent;
import com.example.chat.dto.message.MessageSaveContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractMessageProcessor implements MessageProcessor {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<Void> handleMessage(Object messageRequest) {
        return validateMessage(messageRequest)
                .then(Mono.defer(() -> {
                    MessageSaveContext context = createMessageSaveContext(messageRequest);
                    return saveMessage(context);
                }))
                .flatMap(context -> {
                    MessageCreatedEvent messageCreatedEvent = createMessageCreatedEvent(context);
                    applicationEventPublisher.publishEvent(messageCreatedEvent);
                    return Mono.empty();
                })
                .then();
    }

    /**
     * 메시지 검증
     * @param messageRequest 메시지 요청 객체
     * @return 검증 결과, 오류가 있으면 ErrorMessage 객체를 포함하고, 없으면 Optional.empty()
     */
    protected abstract Mono<Void> validateMessage(Object messageRequest);

    /**
     * 메시지 저장 컨텍스트 생성
     * @param messageRequest 메시지 요청 객체
     * @return 메시지 저장 컨텍스트
     */
    protected abstract MessageSaveContext createMessageSaveContext(Object messageRequest);

    /**
     * 메시지 저장
     * @param messageSaveContext 메시지 저장 컨텍스트
     */
    protected abstract Mono<MessageSaveContext> saveMessage(MessageSaveContext messageSaveContext);

    /**
     * 메시지 생성 이벤트 생성
     * @param messageSaveContext 메시지 저장 컨텍스트
     * @return 메시지 생성 이벤트
     */
    protected abstract MessageCreatedEvent createMessageCreatedEvent(MessageSaveContext messageSaveContext);

}

