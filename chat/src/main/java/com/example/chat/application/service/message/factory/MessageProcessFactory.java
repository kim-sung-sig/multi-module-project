package com.example.chat.application.service.message.factory;

import com.example.chat.application.service.message.handler.MessageProcessor;
import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageProcessFactory {

    private final Map<MessageEntity.MessageType, MessageProcessor> messageHandlers;

    public MessageProcessFactory(List<MessageProcessor> messageProcessors) {
        this.messageHandlers = messageProcessors.stream()
                .collect(Collectors.toMap(MessageProcessor::getMessageType, Function.identity()));
    }

    public MessageProcessor getMessageHandler(MessageEntity.MessageType messageType) {
        return Optional.ofNullable(messageHandlers.get(messageType))
                .map(handler -> {
                    log.debug("messageType: {}, getMessageHandler:{}", messageType, handler.getClass().getSimpleName());
                    return handler;
                })
                .orElseThrow(() -> new IllegalArgumentException("No handler found for message type: " + messageType));
    }

}
