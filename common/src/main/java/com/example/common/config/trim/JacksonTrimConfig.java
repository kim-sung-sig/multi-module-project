package com.example.common.config.trim;

import com.example.common.util.CommonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StringTrimProperties.class)
public class JacksonTrimConfig {

	@Bean
	@ConditionalOnProperty(
			name = "string.trim.enabled",
			havingValue = "true"
	)
	public Module trimStringModule() {
		SimpleModule module = new SimpleModule();
		module.addDeserializer(String.class, new TrimStringDeserializer());
		module.addDeserializer(List.class, new TrimStringListDeserializer());
		module.addDeserializer(Map.class, new TrimStringMapDeserializer());
		return module;
	}


	static class TrimStringDeserializer extends StdDeserializer<String> implements ContextualDeserializer {

		private BeanProperty property;

		public TrimStringDeserializer() {
			super(String.class);
		}

		private TrimStringDeserializer(BeanProperty property) {
			super(String.class);
			this.property = property;
		}

		@Override
		public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			if (property != null && property.getAnnotation(NoTrim.class) != null) {
				return p.getValueAsString(); // trim 안함
			}
			return CommonUtil.safeTrim(p.getValueAsString());
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
			return new TrimStringDeserializer(property);
		}

	}

	static class TrimStringListDeserializer extends StdDeserializer<List<String>> {

		public TrimStringListDeserializer() {
			super(List.class);
		}

		@Override
		public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			List<String> list = ctxt.readValue(p, ctxt.getTypeFactory().constructCollectionType(List.class, String.class));
			if (list == null) return null;
			return list.stream()
					.map(CommonUtil::safeTrim)
					.toList();
		}

	}

	static class TrimStringMapDeserializer extends StdDeserializer<Map<String, String>> {

		public TrimStringMapDeserializer() {
			super(Map.class);
		}

		@Override
		public Map<String, String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			Map<String, String> map = ctxt.readValue(p, ctxt.getTypeFactory().constructMapType(Map.class, String.class, String.class));
			if (map == null) return null;
			return map.entrySet().stream()
					.collect(Collectors.toMap(
							e -> CommonUtil.safeTrim(e.getKey()),
							e -> CommonUtil.safeTrim(e.getValue())
					));
		}

	}

}