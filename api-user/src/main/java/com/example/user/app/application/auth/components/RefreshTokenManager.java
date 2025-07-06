package com.example.user.app.application.auth.components;

import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.Token;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.exception.TokenLimitExceededException;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import com.example.user.app.common.dto.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final RefreshTokenPolicy refreshTokenPolicy;            // 토큰 발급 정책
    private final RefreshTokenRepository refreshTokenRepository;    // Refresh Token Repository

    public RefreshToken issue(SecurityUser user, Device requestDevice) throws TokenLimitExceededException {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(user.getId()).stream()
                .map(RefreshTokenEntity::toDomain)
                .toList();

        // Step 1: 동일 디바이스 확인
        Optional<RefreshToken> maybeSameDeviceToken = tokens.stream()
                .filter(t -> t.getDevice().isSameDevice(requestDevice))
                .findFirst();

        // Step 2: 존재하면 갱신 또는 그대로 사용
        if (maybeSameDeviceToken.isPresent()) {
            RefreshToken token = maybeSameDeviceToken.get();

            if (refreshTokenPolicy.shouldRefresh(token)) {
                token.refresh(createRefreshTokenVal(requestDevice));
                refreshTokenRepository.save(RefreshTokenEntity.fromDomain(token));
            }

            return token;
        }

        // Step 3: 동일 디바이스가 없으면 정책 위반인지 확인 (토큰 수 초과 등)
        if (refreshTokenPolicy.isTokenLimitExceeded(tokens, requestDevice)) {
            throw new TokenLimitExceededException(tokens);
        }

        // Step 4: 신규 발급
        log.debug("[TOKEN SUCCESS] New token issued. userId: {}, device: {}", user.getId(), requestDevice);
        return createNewToken(user, requestDevice);
    }

    private RefreshToken createNewToken(SecurityUser user, Device device) {
        Token refreshToken = createRefreshTokenVal(device);

        RefreshTokenEntity newToken = RefreshTokenEntity.builder()
                .userId(user.getId())
                .device(device)
                .tokenValue(refreshToken.getToken())
                .expiryAt(refreshToken.getExpiry())
                .build();
        refreshTokenRepository.save(newToken);

        log.debug("[TOKEN SUCCESS] New token issued. userId: {}, device: {}, refreshToken: {}", user.getId(), device, refreshToken);
        return newToken.toDomain();
    }

    private Token createRefreshTokenVal(@NonNull Device device) {

        String subject = UUID.randomUUID().toString() + System.currentTimeMillis();

        Map<String, Object> claims = Map.of(
            "random", UUID.randomUUID().toString()
        );

        Instant expiry = Instant.now().plusSeconds(JwtUtil.REFRESH_TOKEN_TTL);

        // 새로운 토큰 발급
        String refreshToken = JwtUtil.generateToken(subject, claims, expiry);
        return new Token(refreshToken, expiry);
    }

    public void delete(RefreshToken refreshToken) {
        refreshTokenRepository.deleteById(refreshToken.getId());
        log.debug("[TOKEN DELETED] Refresh token deleted. id: {}, device: {}", refreshToken.getId(), refreshToken.getDevice());
    }
}
