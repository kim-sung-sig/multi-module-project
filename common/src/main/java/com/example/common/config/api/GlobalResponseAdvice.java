package com.example.common.config.api;

import com.example.common.model.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(@NonNull MethodParameter returnType, @NonNull Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
			Object body,
			@NonNull MethodParameter returnType,
			@NonNull MediaType selectedContentType,
			@NonNull Class selectedConverterType,
			@NonNull ServerHttpRequest request,
			@NonNull ServerHttpResponse response
	) {
		// 응답 통일 wrapper 적용
		if (body instanceof ApiResponse) {
			return body;
		}

		// 파일(Resource) 응답은 감싸면 안 됨
		if (body instanceof Resource) {
			return body;
		}
		if (body instanceof ResponseEntity) {
			Object responseBody = ((ResponseEntity<?>) body).getBody();
			if (responseBody instanceof Resource) {
				return body; // 그대로 통과
			}
		}

		// 그 외는 ApiResponse 로 감싸기
		if (body == null) {
			return ApiResponse.ok(); // void → ok 응답
		}
		return ApiResponse.ok(body);
	}
}