package com.example.user.app.dto.response;

public record JwtTokenResponse(
    String accessToken,
    String refreshToken
) {}
