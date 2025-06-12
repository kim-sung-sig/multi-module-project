package com.example.chat.core.chatcore.domain.model.message;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "image_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageMessage extends Message {

    private String imageUrl;
    private String thumbnailUrl;
    private String fileName;

    public ImageMessage(Long id, Long chatRoomId, Long senderId, String imageUrl, String thumbnailUrl, String fileName) {
        super(id, chatRoomId, senderId);
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.fileName = fileName;
    }

    @Override
    public MessageType getType() {
        return MessageType.IMAGE;
    }

    @Override
    public String getPreviewText() {
        return "사진을 보냈습니다.";
    }
}
