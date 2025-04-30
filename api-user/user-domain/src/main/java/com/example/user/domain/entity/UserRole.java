package com.example.user.domain.entity;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER", "사용자"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String name;

    private UserRole(String key, String title) {
        this.key = key;
        this.name = title;
    }

}
