package com.example.user.app.application.auth.enums;

import com.example.common.interfaces.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // 인증 관련
    UNAUTHORIZED(401, "기본 권한 문제"),
    UNAUTHORIZED_EXPIRED(401, "계정 만료"),
    UNAUTHORIZED_BAD_CREDENTIALS(401, "아이디 또는 비밀번호 틀림"),

    UNAUTHORIZED_DEVICE_NOT_FOUND(401, "디바이스 정보 없음"),
    UNAUTHORIZED_BAD_CREDENTIALS_ACCESS_TOKEN_NOT_FOUND(401, "AccessToken 미존재"),
    UNAUTHORIZED_BAD_REQUEST_REFRESH_TOKEN(401, "RefreshToken is missing"),
    UNAUTHORIZED_INVALID_TOKEN(401, "Token이 유효하지 않음"),
    UNAUTHORIZED_INVALID_TOKEN_DEVICE_MISMATCH(401, "Token 디바이스 불일치"),

    FORBIDDEN_REFRESH_TOKEN_DEVICE_LIMIT_EXCEEDED(403, "디바이스 제한 초과"),
    FORBIDDEN_LOCKED(403, "사용자 계정 잠김"),
    FORBIDDEN(403, "접근 권한 없음");

    private final int code;
    private final String logMessage;

}
