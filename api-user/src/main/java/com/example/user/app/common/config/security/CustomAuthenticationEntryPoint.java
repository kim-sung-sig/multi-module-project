package com.example.user.app.common.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import com.example.common.interfaces.ErrorCode;
import com.example.common.model.ApiResponse;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.auth.exception.CustomAuthenticationException;
import com.example.user.app.common.dto.security.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException
	) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		Object jwtExceptionCandidate = request.getAttribute(SecurityConstant.JWT_EXCEPTION);

		ErrorCode errorCode = jwtExceptionCandidate instanceof CustomAuthenticationException jwtEx
				? jwtEx.getErrorCode()
				: AuthErrorCode.UNAUTHORIZED;
		ApiResponse<Void> apiResponse = ApiResponse.error(errorCode, null);

		try (PrintWriter writer = response.getWriter()) {
			writer.write(objectMapper.writeValueAsString(apiResponse));
		}
	}

}