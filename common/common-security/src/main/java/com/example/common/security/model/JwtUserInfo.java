package com.example.common.security.model;

import java.util.UUID;

public record JwtUserInfo(
    UUID id,
    String username,
    UserRole role
) {}