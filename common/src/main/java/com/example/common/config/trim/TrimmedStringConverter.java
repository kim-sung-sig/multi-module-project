package com.example.common.config.trim;

import com.example.common.util.CommonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
		name = "string.trim.enabled",
		havingValue = "true"
)
public class TrimmedStringConverter implements Converter<String, String> {

	@Override
	public String convert(@Nullable String source) {
		return CommonUtil.safeTrim(source);
	}

}