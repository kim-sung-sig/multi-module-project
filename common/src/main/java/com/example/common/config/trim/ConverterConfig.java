package com.example.common.config.trim;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(StringTrimProperties.class)
public class ConverterConfig {

//	@Bean
//	@ConditionalOnProperty(
//			name = "string.trim.enabled",
//			havingValue = "true"
//	)
//	public Converter<String, String> trimmedStringConverter() {
//		return CommonUtil::safeTrim;
//	}

}