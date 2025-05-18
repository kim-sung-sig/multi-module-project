package com.example.user.app.application.auth.components;

import com.example.common.exception.BaseException;
import com.example.common.model.SecurityUser;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.exception.TokenLimitExceededException;
import com.example.user.app.common.enums.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RefreshTokenManager refreshTokenManager;
    public final static String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public JwtTokenDto get(SecurityUser user, Device device) {

        // Refresh Token 발급
        RefreshToken refreshToken;
        try {
            refreshToken = refreshTokenManager.issue(user, device);
        } catch (TokenLimitExceededException e) {
            throw new BaseException(AuthErrorCode.FORBIDDEN_REFRESH_TOKEN_DEVICE_LIMIT_EXCEEDED, e);
        }

        // Access Token 발급
        String accessToken = createAccessToken(user, device);

        // Token 반환
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", user.getId(), accessToken, refreshToken);
        return new JwtTokenDto(accessToken, refreshToken);
    }

    private String createAccessToken(SecurityUser user, Device device) {

        Map<String, Object> claims = Map.of(
            "id", user.getId().toString(),
            "username", user.getUsername(),
            "permission", user.getPermission(),

            "device", Map.of(
                "deviceId", device.getDeviceId(),
                "platform", device.getPlatform(),
                "browser", device.getBrowser()
            )
        );

        // 새로운 토큰 발급
        return JwtUtil.generateToken(claims, JwtUtil.ACCESS_TOKEN_TTL);
    }

}
