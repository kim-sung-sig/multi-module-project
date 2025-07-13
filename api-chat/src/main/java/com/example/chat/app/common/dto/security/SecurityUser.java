package com.example.chat.app.common.dto.security;

import java.util.List;
import java.util.UUID;


public record SecurityUser (
    UUID id,
    String username,
    String password,
    List<String> permission
) {
    public SecurityUser {
        permission = List.copyOf(permission);
    }
    public String getUsername() {
        return username();
    }

    public String getPassword() {
        return password();
    }

    public UUID getId() {
        return id();
    }

    public List<String> getPermission() {
        return permission();
    }

}
