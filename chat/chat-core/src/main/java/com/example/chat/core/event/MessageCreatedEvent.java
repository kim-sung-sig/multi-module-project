package com.example.chat.core.event;

import com.example.chat.core.infra.entity.message.MessageEntity;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageCreatedEvent extends ApplicationEvent {

    private final MessageEntity messageEntity;

    public MessageCreatedEvent(Object source, MessageEntity messageEntity) {
        super(source);
        this.messageEntity = messageEntity;
    }
}
