package com.example.user.app.application.nickname.enums;

import com.example.common.interfaces.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NickNameErrorCode implements ErrorCode {

    NICKNAME_TAG_NOT_FOUND(404, "닉네임을 찾을 수 없음"),

    // 닉네임 관련
    NICKNAME_EMPTY(400, "닉네임을 입력해주세요"),
    NICKNAME_INVALID_FORMAT(400, "유효한 닉네임이 아닙니다. 닉네임은 영문 대소문자, 숫자, 한글만 사용할 수 있습니다."),
    NICKNAME_TOO_LONG(400, "닉네임이 너무 깁니다"),
    NICKNAME_TOO_SHORT(400, "닉네임이 너무 짧습니다"),
    NICKNAME_CONTAINS_BLANK(400, "닉네임에 공백이 포함되어 있습니다"),
    ;

    private final int code;
    private final String logMessage;

}
