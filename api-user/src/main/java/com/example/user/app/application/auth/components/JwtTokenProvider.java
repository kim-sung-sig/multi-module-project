package com.example.user.app.application.auth.components;

import com.example.common.exception.BaseException;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.dto.Token;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.auth.exception.TokenLimitExceededException;
import com.example.user.app.common.dto.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RefreshTokenManager refreshTokenManager;

    // constants
    public final static String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public final static String REFRESH_TOKEN_HEADER_NAME = "X-Refresh-Token";

    public JwtTokenDto get(SecurityUser user, Device device) {

        // Refresh Token 발급
        RefreshToken refreshToken;
        try {
            refreshToken = refreshTokenManager.issue(user, device);
        } catch (TokenLimitExceededException e) {
            throw new BaseException(AuthErrorCode.FORBIDDEN_REFRESH_TOKEN_DEVICE_LIMIT_EXCEEDED, e);
        }

        // Access Token 발급
        Token accessToken = createAccessToken(user, device);

        // Token 반환
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", user.getId(), accessToken, refreshToken);
        return new JwtTokenDto(
            accessToken.getToken(),
            accessToken.getExpiry(),
            refreshToken);
    }

    private Token createAccessToken(SecurityUser user, Device device) {

        String subject = user.getId().toString();

        Map<String, Object> claims = Map.of(
            "id", user.getId().toString(),
            "username", user.getUsername(),
            "permission", user.getPermission()
        );

        Instant expiry = Instant.now().plusSeconds(JwtUtil.ACCESS_TOKEN_TTL);

        // 새로운 토큰 발급
        String accessToken = JwtUtil.generateToken(subject, claims, expiry);
        return new Token(accessToken, expiry);
    }

}
