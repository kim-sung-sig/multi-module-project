package com.example.user.app.application.auth.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.example.user.app.application.auth.dto.Token;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter @ToString
public class RefreshToken {

    private final UUID id;
    private final UUID userId;
    private final Device device;
    private String tokenValue;
    private Instant expiryAt;
    private Instant createBy;
    private Instant lastUsedAt;

    public boolean isExpiringWithin(Duration duration) {
        return expiryAt.isBefore(Instant.now().plus(duration));
    }

    public void refresh(Token newToken) {
        Instant now = Instant.now();

        this.tokenValue = newToken.getToken();
        this.expiryAt = newToken.getExpiry();
        this.createBy = now;
        this.lastUsedAt = now;
    }

    public void used(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

}
