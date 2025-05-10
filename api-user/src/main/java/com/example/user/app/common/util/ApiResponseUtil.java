package com.example.user.app.common.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.common.enums.ErrorCode;
import com.example.common.model.ApiResponse;

public class ApiResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.<T>ok(data));
    }

    public static <T> ResponseEntity<ApiResponse<Void>> error(ErrorCode errorCode, Map<String, Object> errors) {
        return ResponseEntity
            .status(HttpStatus.valueOf(errorCode.getCode()))
            .body(ApiResponse.error(errorCode, errors));
    }

}
