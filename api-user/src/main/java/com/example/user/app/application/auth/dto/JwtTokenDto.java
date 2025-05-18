package com.example.user.app.application.auth.dto;

import com.example.user.app.application.auth.domain.RefreshToken;

public record JwtTokenDto(
    String accessToken,
    RefreshToken refreshToken
) {}
