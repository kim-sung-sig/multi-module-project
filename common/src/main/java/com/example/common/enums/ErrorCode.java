package com.example.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_REQUEST(400, "Invalid input request"),

    INVALID_TOKEN(401, "Invalid token"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable"),
    TEMPORARY_ERROR(503, "Temporary error");

    private final int code;
    private final String logMessage;

    ErrorCode(int code, String logMessage) {
        this.code = code;
        this.logMessage = logMessage;
    }

}
