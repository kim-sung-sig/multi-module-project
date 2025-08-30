package com.example.common.config.trim;

import com.example.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StringTrimProperties.class)
public class ConverterConfig {

	@Bean
	@ConditionalOnProperty(
			name = "string.trim.enabled",
			havingValue = "true"
	)
	public Converter<String, String> trimmedStringConverter() {
		return CommonUtil::safeTrim;
	}

}