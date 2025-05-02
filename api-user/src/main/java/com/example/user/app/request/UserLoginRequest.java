package com.example.user.app.request;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {}
