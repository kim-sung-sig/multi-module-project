package com.example.common.exception;

import com.example.common.interfaces.ErrorCode;
import lombok.Getter;

/*
 * 400 : 클라이언트 요청 오류
 * 401 : 인증 오류
 * 403 : 권한 오류
 */
@Getter
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String messageDetail;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getLogMessage());
        this.errorCode = errorCode;
        this.messageDetail = null;
    }

    public BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getLogMessage(), cause);
        this.errorCode = errorCode;
        this.messageDetail = cause.getMessage();
    }

    public BaseException(ErrorCode errorCode, String messageDetail) {
        super(errorCode.getLogMessage());
        this.errorCode = errorCode;
        this.messageDetail = messageDetail;
    }

}
