package com.example.user.app.application.auth.dto.request;

import com.example.user.app.application.auth.components.oauth.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthRequest(
    @NotBlank SocialType provider,
    @NotBlank String code,
    String state,
    String redirectUri
) {}
