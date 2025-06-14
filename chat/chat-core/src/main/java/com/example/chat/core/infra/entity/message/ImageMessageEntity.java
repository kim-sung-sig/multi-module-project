package com.example.chat.core.infra.entity.message;

import com.example.chat.core.domain.model.message.MessageType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "image_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageMessageEntity extends MessageEntity {

    private String imageUrl;
    private String thumbnailUrl;
    private String fileName;

    public ImageMessageEntity(Long id, Long chatRoomId, Long senderId, String imageUrl, String thumbnailUrl, String fileName) {
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
