package com.example.user.app.request;

import jakarta.validation.constraints.NotBlank;

public record TokenValidateRequest(
    @NotBlank String accessToken
) {}
