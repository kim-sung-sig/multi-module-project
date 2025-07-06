package com.example.chat.dto.message;

import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MessageProcessingContext {

    private final MessageSendRequest request;

    @Setter
    private MessageEntity messageToSave;

    @Setter
    private MessageEntity savedMessage;

    public MessageProcessingContext(MessageSendRequest request) {
        this.request = request;
    }

}
