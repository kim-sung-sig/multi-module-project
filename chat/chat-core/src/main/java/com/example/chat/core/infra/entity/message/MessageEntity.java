package com.example.chat.core.infra.entity.message;

import com.example.chat.core.domain.model.message.MessageType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("dn_message")
public class MessageEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("type")
    private MessageType type;

    @Column("room_id")
    private Long roomId;

    @Column("sender_id")
    private Long senderId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("details")
    private String details;

}
