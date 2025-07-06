package com.example.chat.application.facade;

import com.example.chat.application.service.message.factory.MessageProcessFactory;
import com.example.chat.application.service.message.handler.MessageProcessor;
import com.example.chat.dto.message.MessageSendRequest;
import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatMessageProcessor {

    private final MessageProcessFactory messageProcessFactory;

    public Mono<MessageEntity> process(MessageSendRequest sendRequest) {

        MessageProcessor messageProcessor = messageProcessFactory.getMessageHandler(sendRequest.getMessageType());

        messageProcessor.handleMessage(sendRequest);

        return Mono.empty();
    }
}
