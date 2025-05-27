package com.example.user.app.application.auth.dto.response;

import java.time.Instant;

import com.example.user.app.application.auth.domain.Device;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenResponse{
    private final String accessToken;
    private final Instant accessTokenExpiry;
    private final Device device;
}
