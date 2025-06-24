package com.example.chat.core.service.message.handler;

import com.example.chat.core.domain.model.message.MessageType;
import reactor.core.publisher.Mono;

public interface MessageHandler {

    MessageType getMessageType();

    Mono<Void> handleMessage(Object messageRequest);

}
