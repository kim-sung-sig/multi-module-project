package com.example.user.app.response;

public record JwtTokenResponse(
    String accessToken,
    String refreshToken
) {}
