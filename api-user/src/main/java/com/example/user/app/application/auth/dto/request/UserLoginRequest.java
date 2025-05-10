package com.example.user.app.application.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {}
