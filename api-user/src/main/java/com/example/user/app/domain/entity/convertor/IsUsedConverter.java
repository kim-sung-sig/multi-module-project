package com.example.user.app.domain.entity.convertor;

import com.example.common.enums.IsUsed;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class IsUsedConverter implements AttributeConverter<IsUsed, String> {

    @Override
    public String convertToDatabaseColumn(IsUsed attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public IsUsed convertToEntityAttribute(String dbData) {
        return dbData != null ? IsUsed.valueOf(dbData) : null;
    }

}
