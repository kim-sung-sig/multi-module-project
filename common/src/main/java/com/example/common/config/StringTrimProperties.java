package com.example.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "string.trim")
public class StringTrimProperties {

	private boolean enabled = false;

}