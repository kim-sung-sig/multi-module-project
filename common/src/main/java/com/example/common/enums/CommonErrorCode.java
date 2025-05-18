package com.example.common.enums;

import com.example.common.interfaces.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // 인풋 및 유효성 관련
    INVALID_INPUT_REQUEST(400, "유효한 입력값이 아닙니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류"),
    TEMPORARY_ERROR(503, "임시적 오류");

    private final int code;
    private final String logMessage;

}
