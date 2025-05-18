package com.example.user.app.common.util;

import com.example.common.enums.CommonErrorCode;
import com.example.common.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ApiResponseUtil {

    public static ResponseEntity<ApiResponse<Void>> ok() {
        return ResponseEntity.ok(ApiResponse.ok());
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    public static ResponseEntity<ApiResponse<Void>> error(CommonErrorCode commonErrorCode, Map<String, Object> errors) {
        return ResponseEntity
            .status(HttpStatus.valueOf(commonErrorCode.getCode()))
            .body(ApiResponse.error(commonErrorCode, errors));
    }

}
