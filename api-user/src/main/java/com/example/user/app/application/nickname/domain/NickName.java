package com.example.user.app.application.nickname.domain;

import java.util.Optional;
import java.util.function.Supplier;

import com.example.common.exception.BaseException;
import com.example.common.util.CommonUtil;
import com.example.user.app.application.nickname.enums.NickNameErrorCode;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class NickName implements Supplier<String> {

    private final String value;

    public NickName(String value) {
        validateFormat(value);
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }

    private void validateFormat(String value) {
        // 값 존재 여부
        if (CommonUtil.isEmpty(value)) {
            throw new BaseException(NickNameErrorCode.NICKNAME_EMPTY);
        }

        // 공백 여부
        if (CommonUtil.hasBlank(value)) {
            throw new BaseException(NickNameErrorCode.NICKNAME_CONTAINS_BLANK);
        }

        // 문자 길이 여부
        if (value.length() < 2) {
            throw new BaseException(NickNameErrorCode.NICKNAME_TOO_SHORT);
        }
        if (value.length() > 16) {
            throw new BaseException(NickNameErrorCode.NICKNAME_TOO_LONG);
        }

        // 특수 문자 여부
        if (!value.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new BaseException(NickNameErrorCode.NICKNAME_INVALID_FORMAT);
        }
    }

    public static Optional<NickName> tryCreate(String value) {
        try {
            return Optional.of(new NickName(value));
        } catch (BaseException e) {
            return Optional.empty();
        }
    }
}
