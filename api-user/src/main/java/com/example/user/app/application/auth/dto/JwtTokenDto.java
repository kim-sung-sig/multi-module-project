package com.example.user.app.application.auth.dto;

import java.time.Instant;

import com.example.user.app.application.auth.domain.RefreshToken;

public record JwtTokenDto(
    String accessToken,
    Instant accessTokenExpiry,
    RefreshToken refreshToken
) {}
