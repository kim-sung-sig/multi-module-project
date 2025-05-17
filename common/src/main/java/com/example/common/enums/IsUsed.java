package com.example.common.enums;

import lombok.Getter;

@Getter
public enum IsUsed {

    ENABLED("사용"),
    DISABLED("미사용"),
    DELETED("삭제");

    private final String name;

    IsUsed(String name) {
        this.name = name;
    }

}
