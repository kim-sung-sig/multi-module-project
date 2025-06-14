package com.example.chat.core.domain.repository.message;

import com.example.chat.core.infra.entity.message.MessageEntity;

public interface MessageWriter {
    void write(MessageEntity message);
}
