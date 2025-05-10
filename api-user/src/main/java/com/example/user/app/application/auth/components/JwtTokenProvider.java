package com.example.user.app.application.auth.components;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.common.model.SecurityUser;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenResponse getTokenResponseWithContinuous(SecurityUser user) {

        // 새로운 토큰 발급
        String accessToken = getAccessToken(user);
        String refreshToken = reuseOrRenewRefreshToken(user);

        // 토큰 반환
        return new JwtTokenResponse(accessToken, refreshToken);
    }

    public JwtTokenResponse getTokenResponseWithDeletion(SecurityUser user) {

        // 새로운 토큰 발급
        String accessToken = getAccessToken(user);
        String refreshToken = invalidateAndIssueRefreshToken(user);

        // 토큰 반환
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", user.getId(), accessToken, refreshToken);
        return new JwtTokenResponse(accessToken, refreshToken);
    }

    private String getAccessToken(SecurityUser user) {

        Map<String, Object> claims = Map.of(
            "id", user.getId().toString(),
            "username", user.getUsername(),
            "permission", user.getPermission()
        );

        // 새로운 토큰 발급
        return JwtUtil.generateToken(claims, JwtUtil.ACCESS_TOKEN_TTL);
    }

    private String reuseOrRenewRefreshToken(SecurityUser user) {
        return refreshTokenRepository.findByUserId(user.getId()).stream()
                .findFirst()
                .map(token -> {
                    if (!token.isExpiringWithinOneMonth()) {
                        return token.getRefreshToken();
                    }

                    refreshTokenRepository.delete(token);
                    return createAndSaveRefreshToken(user);
                })
                .orElseGet(() -> createAndSaveRefreshToken(user));
    }

    private String invalidateAndIssueRefreshToken(SecurityUser user) {
        var tokenList = refreshTokenRepository.findByUserId(user.getId());
        refreshTokenRepository.deleteAll(tokenList);

        return createAndSaveRefreshToken(user);
    }

    private String createAndSaveRefreshToken(SecurityUser user) {
        String token = JwtUtil.generateRefreshToken(JwtUtil.REFRESH_TOKEN_TTL);
        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .userId(user.getId())
                .refreshToken(token)
                .expiryAt(JwtUtil.getExpiration(token).toInstant())
                .build();
        refreshTokenRepository.save(entity);
        return token;
    }

}
