package com.example.user.app.infra.entity.convertor;

import com.example.user.app.infra.entity.UserRole;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {

    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        return dbData != null ? UserRole.valueOf(dbData) : null;
    }

}
