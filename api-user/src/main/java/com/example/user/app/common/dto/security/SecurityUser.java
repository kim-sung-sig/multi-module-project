package com.example.user.app.common.dto.security;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


@Data
@Setter(value = AccessLevel.NONE)
public class SecurityUser {
    private final UUID id;
    private final String username;
    private final String password;
    private final List<String> permission;

    public SecurityUser(UUID id, String username, String password, List<String> permission) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.permission = List.copyOf(permission);
    }

    public boolean validatePassword(PasswordEncoder encoder, String rawPassword) {
        return encoder.matches(rawPassword, this.getPassword());
    }

}
