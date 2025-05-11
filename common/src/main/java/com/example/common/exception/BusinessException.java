package com.example.common.exception;

import com.example.common.enums.ErrorCode;
import lombok.Getter;

/*
 * 400 : 클라이언트 요청 오류
 * 401 : 인증 오류
 * 403 : 권한 오류
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String messageDetail;

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getLogMessage(), cause);
        this.errorCode = errorCode;
        this.messageDetail = cause.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String messageDetail) {
        super(errorCode.getLogMessage());
        this.errorCode = errorCode;
        this.messageDetail = messageDetail;
    }

}
