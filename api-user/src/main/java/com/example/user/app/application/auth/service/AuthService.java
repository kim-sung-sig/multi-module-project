package com.example.user.app.application.auth.service;

import java.util.Collections;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.common.model.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import com.example.user.app.application.user.entity.User;
import com.example.user.app.application.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // repository
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;              // jwt component

    /** 토큰 발급 (username, password)
     * @param loginRequest
     * @return
     */
    @Transactional
    public JwtTokenResponse createTokenByUsernameAndPassword(String inputUsername, String inputPassword) {

        // TODO exception 변경하기
        User user = userRepository.findByUsername(inputUsername)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // 사용자의 비밀번호 일치여부 확인
        if (!passwordEncoder.matches(inputPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // if (!securityUser.isEnabled()) {
        //     log.warn("[SECURITY WARNING] Disabled user attempted to login. username: {}, userId: {}", inputUsername, user.getId());
        //     throw new BadCredentialsException("User account is disabled");
        // }
        
        // if (!securityUser.isAccountNonLocked()) {
        //     log.warn("[SECURITY WARNING] Locked user attempted to login. username: {}, userId: {}", inputUsername, user.getId());
        //     throw new LockedException("User account is locked");
        // }

        SecurityUser securityUser = new SecurityUser(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(user.getRole().name())
        );

        JwtTokenResponse token = jwtTokenProvider.getTokenResponseWithDeletion(securityUser);
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", securityUser.id(), token.accessToken(), token.refreshToken());
        return token;
    }

    /** 토큰 발급 (refreshToken)
     * @param refreshToken
     * @return
     */
    @Transactional
    public JwtTokenResponse createTokenByRefreshToken(String refreshToken) {
        // 1. 토큰이 비어있으면 400 에러
        if (CommonUtil.isEmpty(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token is missing");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is missing");
        }

        // 2. 토큰 검증 실패 시 401 에러
        if (!JwtUtil.validateToken(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token({}) is unvalid or expired", refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        // 3. 토큰 저장소 조회 (없으면 401 에러)
        UUID userId = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RefreshTokenEntity::getUserId)
                .orElseThrow(() -> {
                    log.debug("[TOKEN ERROR] Refresh token({}) not found in repository", refreshToken);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
                });

        // 4. 유저 조회 (없으면 401 에러)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.debug("[USER ERROR] User not found for refreshToken: {}, userId: {}", refreshToken, userId);
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
                });

        // 5. 인증 정보 객체 변환 TODO: Security상위 객체 필요 상속 필요
        SecurityUser securityUser = new SecurityUser(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(user.getRole().name())
        );
        // if (!securityUser.isEnabled()) {
        //     log.warn("[SECURITY WARNING] Disabled user attempted to refresh token. userId: {}, refreshToken: {}", userId, refreshToken);
        //     throw new BadCredentialsException("User account is disabled");
        // }

        // if (!securityUser.isAccountNonLocked()) {
        //     log.warn("[SECURITY WARNING] Locked user attempted to refresh token. userId: {}, refreshToken: {}", userId, refreshToken);
        //     throw new LockedException("User account is locked");
        // }

        // 6. 새로운 토큰 발급 (성공 로그)
        log.info("[TOKEN SUCCESS] Refresh token({}) validated. Issuing new token for userId: {}", refreshToken, userId);

        JwtTokenResponse token = jwtTokenProvider.getTokenResponseWithContinuous(securityUser);
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", userId, token.accessToken(), token.refreshToken());
        return token;
    }

    @Transactional
    public void logout(UUID userId) {
        // 1. 리프래쉬 토큰 삭제
        // refreshTokenRepository.deleteRefreshToken(userId);

        // 2. 로그아웃 성공 로그
        log.info("[LOGOUT SUCCESS] User logged out. userId: {}", userId);
    }

}
