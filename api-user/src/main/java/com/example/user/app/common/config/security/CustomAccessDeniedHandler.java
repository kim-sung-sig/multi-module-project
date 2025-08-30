package com.example.user.app.common.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.common.model.ApiResponse;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException
	) throws IOException {

		ApiResponse<Void> apiResponse = ApiResponse.error(AuthErrorCode.FORBIDDEN, null);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		try (PrintWriter writer = response.getWriter()) {
			writer.write(objectMapper.writeValueAsString(apiResponse));
		}
	}

}