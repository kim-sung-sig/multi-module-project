package com.example.user.app.application.auth.exception;

import com.example.common.interfaces.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException {

	private final ErrorCode errorCode;

	public CustomAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getLogMessage());
		this.errorCode = errorCode;
	}

}