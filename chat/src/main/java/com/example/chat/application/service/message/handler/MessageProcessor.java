package com.example.chat.application.service.message.handler;

import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import reactor.core.publisher.Mono;

public interface MessageProcessor {

    MessageEntity.MessageType getMessageType();

    Mono<Void> handleMessage(Object messageRequest);

}
