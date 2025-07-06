package com.example.chat.application.service.message;

import com.example.chat.application.service.message.factory.MessageHandlerFactory;
import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageHandlerFactory messageHandlerFactory;

    /**
     * 메시지 요청을 처리합니다.
     * @param messageRequest 메시지 요청 객체
     */
    public Mono<Void> processMessage(Object messageRequest) {
        // 메시지 발송 여부 확인

        // 메시지 타입 추출
        MessageEntity.MessageType messageType = null;
        return Mono.just(messageHandlerFactory.getMessageHandler(messageType))
                .flatMap(handler -> handler.handleMessage(messageRequest));
    }

}
