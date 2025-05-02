package com.example.user.app.application.token;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.example.common.model.SecurityUser;
import com.example.user.app.domain.repository.RefreshTokenRepository;
import com.example.user.app.response.JwtTokenResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Jwt 토큰 발급간 비지니스로직처리
     *
     * @param user
     * @return
     */
    public JwtTokenResponse createToken(SecurityUser user) {
        AtomicReference<String> refreshToken = new AtomicReference<>();

        refreshTokenRepository.findByUserId(user.id());

                // .ifPresentOrElse(existedToken -> {
                //     Date expiration = JwtUtil.getExpiration(existedToken);

                //     // 만료일이 1달미만으로 남은 경우 재발급
                //     if (isExpiringWithinOneMonth(expiration)) {
                //         String newToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
                //         refreshTokenRepository.deleteRefreshToken(user.id());
                //         refreshTokenRepository.saveRefreshToken(newToken, user.id());
                //         refreshToken.set(JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL));
                //     }
                //     else refreshToken.set(existedToken);
                // }, () -> {
                //     String newToken = JwtUtil.generateRefreshToken(ConstantsUtil.REFRESH_TOKEN_TTL);
                //     refreshTokenRepository.saveRefreshToken(newToken, user.id());
                //     refreshToken.set(newToken);
                // });

        // 새로운 토큰 발급
        String accessToken = "";//JwtUtil.generateToken(user, ConstantsUtil.ACCESS_TOKEN_TTL);

        // 토큰 반환
        return new JwtTokenResponse(accessToken, refreshToken.get());
    }

    // 유틸 메서드: 1달 이내인지 확인
    private boolean isExpiringWithinOneMonth(Date expiration) {
        Instant expDateTime = expiration.toInstant();
        Instant oneMonthLater = Instant.now().plus(30L, ChronoUnit.DAYS);
        return expDateTime.isBefore(oneMonthLater);
    }

}
