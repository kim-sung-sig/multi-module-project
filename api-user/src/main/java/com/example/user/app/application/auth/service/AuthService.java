package com.example.user.app.application.auth.service;

import java.util.UUID;

import com.example.user.app.application.auth.dto.SecurityUserDetail;
import com.example.user.app.common.config.AccessTokenBlackListProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.enums.ErrorCode;
import com.example.common.exception.BusinessException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.components.LoginComponent;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // repository
    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginComponent loginComponent;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;                            // jwt component
    private final AccessTokenBlackListProvider accessTokenBlackListProvider;

    /**
     * 토큰 발급 (username, password)
     */
    @Transactional
    public JwtTokenResponse loginWithUsernameAndPassword(String inputUsername, String inputPassword) {
        final var exception = new BusinessException(ErrorCode.UNAUTHORIZED, "아이디 또는 비밀번호가 틀립니다.");

        // 1. 유저 조회
        var securityUser = loginComponent.loadByUsername(inputUsername)
                .orElseThrow(() -> exception);

        // 2. 상태검증
        checkUserStatus(securityUser, exception);

        // 3. 사용자의 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(inputPassword, securityUser.getPassword())) {
            loginComponent.loginFail(securityUser);
            throw exception;
        }

        // 4. 로그인 성공 처리
        loginComponent.loginSuccess(securityUser);

        // 5. 새로운 토큰 발급
        return jwtTokenProvider.getTokenResponseWithDeletion(securityUser);
    }

    /**
     * 토큰 발급 (refreshToken)
     */
    @Transactional
    public JwtTokenResponse reissueToken(String refreshToken) {
        final var exception = new BusinessException(ErrorCode.UNAUTHORIZED, "Refresh token invalid or expired");

        // 1. 토큰이 비어있으면 400 에러
        if (CommonUtil.isEmpty(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token is missing");
            throw new BusinessException(ErrorCode.INVALID_INPUT_REQUEST, "Refresh token is missing");
        }

        // 2. 토큰 검증 실패 시 401 에러
        if (!JwtUtil.validateToken(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token({}) is invalid or expired", refreshToken);
            throw exception;
        }

        // 3. 토큰 저장소 조회 (없으면 401 에러)
        UUID userId = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RefreshTokenEntity::getUserId)
                .orElseThrow(() -> {
                    log.warn("[TOKEN ERROR] Refresh token({}) not found in repository", refreshToken);
                    return exception;
                });

        // 4. 유저 조회 (없으면 401 에러)
        var securityUser = loginComponent.loadById(userId)
                .orElseThrow(() -> exception);

        // 5. 유저 상태검증
        checkUserStatus(securityUser, exception);

        // 6. 새로운 토큰 발급
        return jwtTokenProvider.getTokenResponseWithContinuous(securityUser);
    }

    private void checkUserStatus(SecurityUserDetail securityUser, BusinessException exception) {
        if (!securityUser.isEnabled()) {
            log.warn("[SECURITY WARNING] Disabled user attempted to log in. userId: {}", securityUser.getId());
            throw exception;
        }

        if (!securityUser.isAccountNonLocked()) {
            log.warn("[SECURITY WARNING] Locked user attempted to log in. userId: {}", securityUser.getId());
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "계정이 잠겨있는 상태입니다.");
        }
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(UUID userId, String accessToken) {
        // 1. 리프래쉬 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);

        // 2. accessToken 블랙리스트 등록
        accessTokenBlackListProvider.add(accessToken);

        // 3. 로그아웃 성공 로그
        log.info("[LOGOUT SUCCESS] User logged out. userId: {}, accessToken: {}", userId, accessToken);
    }

}
