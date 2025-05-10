package com.example.user.app.application.nickname.domain;

import java.util.Optional;
import java.util.function.Supplier;

import com.example.common.util.CommonUtil;

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
            throw new IllegalArgumentException("닉네임을 입력해주세요.");
        }

        // 공백 여부
        if (CommonUtil.hasBlank(value)) {
            throw new IllegalArgumentException("닉네임에 공백에 포함될 수 없습니다.");
        }

        // 문자 길이 여부
        if (value.length() < 2 || value.length() > 16) {
            throw new IllegalArgumentException("닉네임은 2자 이상 16자 이하만 가능합니다.");
        }

        // 특수 문자 여부
        if (!value.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new IllegalArgumentException("유효한 닉네임이 아닙니다. 닉네임은 영문 대소문자, 숫자, 한글만 사용할 수 있습니다.");
        }
    }

    public static Optional<NickName> tryCreate(String value) {
        try {
            return Optional.of(new NickName(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
