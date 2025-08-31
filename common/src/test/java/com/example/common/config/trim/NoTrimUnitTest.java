package com.example.common.config.trim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoTrimUnitTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new JacksonTrimConfig.TrimStringDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    @DisplayName("필드에 @NoTrim이 있으면 해당 필드는 trim 안함")
    void fieldWithNoTrimShouldSkipTrimming() throws Exception {
        String json = """
            {
                "fieldOnly": "  test  ",
                "normal": "  normal  "
            }
            """;

        TestDto1 result = objectMapper.readValue(json, TestDto1.class);

        assertThat(result.fieldOnly).isEqualTo("  test  ");
        assertThat(result.normal).isEqualTo("normal");
    }

    @Test
    @DisplayName("클래스에 @NoTrim이 있으면 모든 필드 trim 안함")
    void classWithNoTrimShouldSkipTrimming() throws Exception {
        String json = """
            {
                "field": "  test  "
            }
            """;

        TestDto2 result = objectMapper.readValue(json, TestDto2.class);

        assertThat(result.field).isEqualTo("  test  ");
    }

    @Test
    @DisplayName("클래스와 필드 모두 @NoTrim이 있으면 trim 안함")
    void bothClassAndFieldWithNoTrimShouldSkipTrimming() throws Exception {
        String json = """
            {
                "bothNoTrim": "  test  ",
                "normal": "  normal  "
            }
            """;

        TestDto3 result = objectMapper.readValue(json, TestDto3.class);

        assertThat(result.bothNoTrim).isEqualTo("  test  ");
        assertThat(result.normal).isEqualTo("  normal  ");
    }

    static class TestDto1 {
        @NoTrim
        public String fieldOnly;
        public String normal;
    }

    @NoTrim
    static class TestDto2 {
        public String field;
    }

    @NoTrim
    static class TestDto3 {
        @NoTrim
        public String bothNoTrim;
        public String normal;
    }
}