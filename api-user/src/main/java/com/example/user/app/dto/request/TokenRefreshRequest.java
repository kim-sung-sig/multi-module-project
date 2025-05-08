package com.example.user.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
    @NotBlank String refreshToken
) {}
