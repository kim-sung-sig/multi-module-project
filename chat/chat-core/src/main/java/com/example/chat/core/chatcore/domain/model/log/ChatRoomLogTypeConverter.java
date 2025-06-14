package com.example.chat.core.chatcore.domain.model.log;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ChatRoomLogTypeConverter implements AttributeConverter<ChatRoomLogType, String> {
    @Override
    public String convertToDatabaseColumn(ChatRoomLogType attribute) {
        return attribute.name();
    }

    @Override
    public ChatRoomLogType convertToEntityAttribute(String dbData) {
        try {
            return ChatRoomLogType.valueOf(dbData);
        }
        catch (Exception e) {
            return null;
        }
    }
}
