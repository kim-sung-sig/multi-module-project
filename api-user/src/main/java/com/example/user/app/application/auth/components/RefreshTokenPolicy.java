package com.example.user.app.application.auth.components;

import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.entity.Device;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RefreshTokenPolicy {

    public boolean isTokenLimitExceeded(Collection<RefreshToken> existingTokens, Device device) {
        boolean exists = existingTokens.stream().anyMatch(t -> t.isSameDevice(device));

        // 최대 발급 가능한 Refresh Token 수
        int maxTokensPerUser = 5;

        // 동일 디바이스가 존재 또는 최대 디바이스 수 초과
        return !exists && existingTokens.size() >= maxTokensPerUser;
    }

    public boolean shouldRefresh(RefreshToken refreshToken) {
        return refreshToken.isExpiringWithin(java.time.Duration.ofDays(30));
    }
}
