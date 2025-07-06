package com.example.chat.infra.persistence.message;

import com.example.chat.infra.entity.message.r2dbc.MessageEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends R2dbcRepository<MessageEntity, Long> {
}
