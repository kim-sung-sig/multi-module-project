package com.example.user.app.application.auth.domain;

import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.entity.Device;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

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

    public boolean isSameDevice(Device otherDevice) {
        return this.device.equals(otherDevice);
    }

    public void refresh(String newTokenValue) {
        Instant now = Instant.now();

        this.tokenValue = newTokenValue;
        this.expiryAt = JwtUtil.getExpiration(newTokenValue).toInstant();
        this.createBy = now;
        this.lastUsedAt = now;
    }

    public void used(Instant lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

}
