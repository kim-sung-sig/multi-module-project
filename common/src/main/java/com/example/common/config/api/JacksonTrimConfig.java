package com.example.common.config.api;

import com.example.common.anotations.NoTrim;
import com.example.common.util.CommonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonTrimConfig {

	@Bean
	@ConditionalOnProperty(
			name = "string.trim.enabled",
			havingValue = "true"
	)
	public Module trimModule() {
		SimpleModule module = new SimpleModule();
		// String trim
		module.addDeserializer(String.class, new TrimStringDeserializer());

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
			String value = p.getValueAsString();
			if (shouldSkipTrim()) {
				return value;
			}
			return CommonUtil.safeTrim(value);
		}

		private boolean shouldSkipTrim() {
			if (property == null) return false;

			boolean hasFieldNoTrim = property.getAnnotation(NoTrim.class) != null;
			boolean hasClassNoTrim = property.getMember() != null &&
					property.getMember().getDeclaringClass().isAnnotationPresent(NoTrim.class);

			return hasFieldNoTrim || hasClassNoTrim;
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
			return new TrimStringDeserializer(property);
		}

	}

}