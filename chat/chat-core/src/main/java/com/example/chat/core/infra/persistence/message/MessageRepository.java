package com.example.chat.core.infra.persistence.message;

import com.example.chat.core.infra.entity.message.MessageEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends R2dbcRepository<MessageEntity, Long> {
}
