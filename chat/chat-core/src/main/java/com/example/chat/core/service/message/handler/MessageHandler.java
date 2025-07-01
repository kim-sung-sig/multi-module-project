package com.example.chat.core.service.message.handler;

import com.example.chat.core.infra.entity.message.r2dbc.MessageEntity;
import reactor.core.publisher.Mono;

public interface MessageHandler {

    MessageEntity.MessageType getMessageType();

    Mono<Void> handleMessage(Object messageRequest);

}
