package com.example.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Getter
@EnableConfigurationProperties(StringTrimProperties.class)
@ConfigurationProperties(prefix = "string.trim")
public class StringTrimProperties {

	private final boolean enabled = false;

}
