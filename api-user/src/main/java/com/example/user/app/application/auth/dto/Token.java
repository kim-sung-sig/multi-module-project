package com.example.user.app.application.auth.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private final String token;
    private final Instant expiry;
}

