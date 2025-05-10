package com.example.common.model;

import java.util.Map;

import com.example.common.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int code,
    String message,
    T data,
    Map<String, Object> errors
) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
            200,
            "OK",
            data,
            null
        );
    }

    public static ApiResponse<Void> error(ErrorCode errorCode, Map<String, Object> errors) {
        return new ApiResponse<>(
            errorCode.getCode(),
            errorCode.getLogMessage(),
            null,
            errors
        );
    }
}
