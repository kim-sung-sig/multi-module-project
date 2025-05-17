package com.example.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_REQUEST(400, "Invalid input request"),

    UNAUTHORIZED(401, "Unauthorized"),

    UNAUTHORIZED_LOCKED(401, "Unauthorized locked"),
    UNAUTHORIZED_EXPIRED(401, "Unauthorized expired"),
    UNAUTHORIZED_BAD_CREDENTIALS(401, "Unauthorized bad credentials"),
    UNAUTHORIZED_INVALID_ACCESS_TOKEN(401, "Invalid token"),
    UNAUTHORIZED_INVALID_REFRESH_TOKEN(401, "Unauthorized invalid refresh token"),

    FORBIDDEN_REFRESH_TOKEN_DEVICE_LIMIT_EXCEEDED(403, "Device limit exceeded for refresh token issuance"),
    FORBIDDEN(403, "Forbidden"),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    TEMPORARY_ERROR(503, "Temporary error");

    private final int code;
    private final String logMessage;

    ErrorCode(int code, String logMessage) {
        this.code = code;
        this.logMessage = logMessage;
    }

}
