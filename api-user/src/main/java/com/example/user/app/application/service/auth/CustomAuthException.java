package com.example.user.app.application.service.auth;

import lombok.Getter;

@Getter
public class CustomAuthException extends RuntimeException{

    private final AuthErrorCode code;

    public CustomAuthException(AuthErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public enum AuthErrorCode {
        BAD_CREDENTIALS,
        ACCOUNT_LOCKED,
        REFRESH_TOKEN_EXPIRED,
        USER_NOT_FOUND,
        OAUTH2_AUTH_FAILED
    }

}
