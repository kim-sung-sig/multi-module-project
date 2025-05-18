package com.example.common.model;

import com.example.common.interfaces.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int code,
    String message,
    T data,
    Map<String, Object> errors
) {

    public static <Void> ApiResponse<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
            200,
            "OK",
            data,
            null
        );
    }

    public static ApiResponse<Void> error(ErrorCode commonErrorCode, Map<String, Object> errors) {
        return new ApiResponse<>(
            commonErrorCode.getCode(),
            commonErrorCode.getLogMessage(),
            null,
            errors
        );
    }
}
