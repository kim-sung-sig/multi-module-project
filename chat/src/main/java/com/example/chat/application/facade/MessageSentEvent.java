package com.example.chat.application.facade;

import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageSentEvent extends ApplicationEvent {

    private final MessageEntity entity;

    public MessageSentEvent(Object source, MessageEntity entity) {
        super(source);
        this.entity = entity;
    }
}
