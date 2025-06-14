package com.example.chat.core.chatcore.domain.repository.message;

import com.example.chat.core.chatcore.infra.entity.message.MessageEntity;

public interface MessageWriter {
    void write(MessageEntity message);
}
