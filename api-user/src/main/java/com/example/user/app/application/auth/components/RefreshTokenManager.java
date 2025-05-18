package com.example.user.app.application.auth.components;

import com.example.common.model.SecurityUser;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.exception.TokenLimitExceededException;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final RefreshTokenPolicy refreshTokenPolicy;            // 토큰 발급 정책
    private final RefreshTokenRepository refreshTokenRepository;    // Refresh Token Repository

    public RefreshToken issue(SecurityUser user, Device device) throws TokenLimitExceededException {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(user.getId()).stream()
                .map(RefreshTokenEntity::toDomain)
                .toList();

        // Step 1: 동일 디바이스 확인
        Optional<RefreshToken> maybeSameDeviceToken = tokens.stream()
                .filter(t -> t.isSameDevice(device))
                .findFirst();

        // Step 2: 존재하면 갱신 또는 그대로 사용
        if (maybeSameDeviceToken.isPresent()) {
            RefreshToken token = maybeSameDeviceToken.get();

            if (refreshTokenPolicy.shouldRefresh(token)) {
                token.refresh(createRefreshTokenVal(device));
                refreshTokenRepository.save(RefreshTokenEntity.fromDomain(token));
            }

            return token;
        }

        // Step 3: 동일 디바이스가 없으면 정책 위반인지 확인 (토큰 수 초과 등)
        if (refreshTokenPolicy.isTokenLimitExceeded(tokens, device)) {
            throw new TokenLimitExceededException(tokens);
        }

        // Step 4: 신규 발급
        log.debug("[TOKEN SUCCESS] New token issued. userId: {}, device: {}", user.getId(), device);
        return createNewToken(user, device);
    }

    private RefreshToken createNewToken(SecurityUser user, Device device) {
        String refreshToken = createRefreshTokenVal(device);
        Instant expiryAt = JwtUtil.getExpiration(refreshToken).toInstant();

        RefreshToken newToken = new RefreshToken(
                null,
                user.getId(),
                device,
                refreshToken,
                expiryAt,
                Instant.now(),
                Instant.now()
        );
        refreshTokenRepository.save(RefreshTokenEntity.fromDomain(newToken));

        log.debug("[TOKEN SUCCESS] New token issued. userId: {}, device: {}, refreshToken: {}", user.getId(), device, refreshToken);
        return newToken;
    }

    private String createRefreshTokenVal(@NonNull Device device) {

        Map<String, Object> claims = Map.of(
                "deviceId", device.getDeviceId(),
                "platform", device.getPlatform(),
                "browser", device.getBrowser()
        );

        // 새로운 토큰 발급
        return JwtUtil.generateToken(claims, JwtUtil.REFRESH_TOKEN_TTL);
    }

}
