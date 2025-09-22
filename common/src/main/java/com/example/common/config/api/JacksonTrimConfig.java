package com.example.common.config.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
public class JacksonTrimConfig {

	@Bean
	public Module trimModule() {
		SimpleModule module = new SimpleModule();

		// String trim
		module.addDeserializer(String.class, new StdDeserializer<>(String.class) {
			@Override
			public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				return Optional.ofNullable(p.getValueAsString())
						.map(String::trim)
						.orElse(null);
			}
		});

		// List 처리
		module.addDeserializer(List.class, new StdDeserializer<List<?>>(List.class) {
			@Override
			public List<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				JavaType javaType = ctxt.getContextualType();
				JavaType contentType = javaType != null ? javaType.getContentType() : null;

				List<?> list = ctxt.readValue(p, ctxt.getTypeFactory().constructCollectionType(
						List.class,
						contentType != null ? contentType.getRawClass() : Object.class
				));

				if (list == null) list = new ArrayList<>();
				TrimUtil.trimObject(list);
				return list;
			}
		});

		// Map 처리
		module.addDeserializer(Map.class, new StdDeserializer<Map<?, ?>>(Map.class) {
			@Override
			public Map<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
				Map<?, ?> map = ctxt.readValue(p, ctxt.getTypeFactory().constructMapType(Map.class, Object.class, Object.class));
				if (map == null) map = new HashMap<>();
				TrimUtil.trimObject(map);
				return map;
			}
		});

		return module;
	}
}