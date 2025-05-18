package com.example.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class ObjectMapperUtil {

    private static final ObjectMapper DEFAULT_MAPPER;
    private static final ObjectMapper PRETTY_MAPPER;

    private ObjectMapperUtil() {}

    static {
        DEFAULT_MAPPER = createDefaultMapper(false);
        PRETTY_MAPPER = createDefaultMapper(true);
    }

    private static ObjectMapper createDefaultMapper(boolean pretty) {
        ObjectMapper mapper = new ObjectMapper();

        // 공통 설정
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true); // enum 안전 처리
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModules(); // JavaTimeModule 등 등록 자동화

        if (pretty) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        return mapper;
    }

    public static ObjectMapper getInstance() {
        return DEFAULT_MAPPER;
    }

    public static ObjectMapper getPrettyInstance() {
        return PRETTY_MAPPER;
    }
}
