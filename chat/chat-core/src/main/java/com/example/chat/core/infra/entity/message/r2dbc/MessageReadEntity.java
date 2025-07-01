package com.example.chat.core.infra.entity.message.r2dbc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "dn_message_read")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageReadEntity {

    @Id
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("message_id")
    private Long messageId;

    @CreatedDate
    @Column("read_at")
    private LocalDateTime readAt;

    public MessageReadEntity(Long messageId, Long userId) {
        this.messageId = messageId;
        this.userId = userId;
        this.readAt = LocalDateTime.now();
    }
}
