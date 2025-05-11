package com.example.user.app.application.auth.components.oauth;

import lombok.Getter;

@Getter
public class OAuth2Exception extends RuntimeException{

    private final OAuth2ErrorCode errorCode;
    private final String messageDetail;

    public OAuth2Exception(OAuth2ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.messageDetail = message;
    }

    @Getter
    public enum OAuth2ErrorCode{
        CLIENT,
        PROVIDER,
        SERVER
    }
}
