package com.example.user.app.application.auth.dto.request;

import com.example.user.app.application.auth.domain.Device;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OAuthRequestWrapper(
    @Valid @NotNull OAuthRequest oAuth,
    @Valid @NotNull Device device
) {}
