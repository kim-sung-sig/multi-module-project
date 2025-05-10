package com.example.user.app.application.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthRequest(
    @NotBlank String provider,
    @NotBlank String code,
    String state,
    String redirectUri
) {}
