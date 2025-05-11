package com.example.user.app.application.auth.exception;

import lombok.Getter;

@Getter
public class OAuth2Exception extends RuntimeException{

    private final AuthErrorCode code;

    public OAuth2Exception(AuthErrorCode code, String message) {
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
