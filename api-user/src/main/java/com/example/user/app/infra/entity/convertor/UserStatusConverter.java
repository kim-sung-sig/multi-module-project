package com.example.user.app.infra.entity.convertor;

import com.example.user.app.infra.entity.UserStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {

    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? UserStatus.valueOf(dbData) : null;
    }

}