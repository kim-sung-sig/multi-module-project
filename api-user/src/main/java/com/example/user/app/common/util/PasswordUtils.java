package com.example.user.app.common.util;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

    private static PasswordEncoder passwordEncoder;

    public PasswordUtils(PasswordEncoder passwordEncoder) {
        PasswordUtils.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (passwordEncoder == null) {
            throw new RuntimeException("PasswordEncoder is null");
        }
    }

    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean matches(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
