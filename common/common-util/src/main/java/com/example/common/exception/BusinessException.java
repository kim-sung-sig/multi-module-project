package com.example.common.exception;

public class BusinessException extends RuntimeException {

    // 기본 생성자 (단순 메시지만 전달할 경우)
    public BusinessException(String message) {
        super(message);
    }

}
