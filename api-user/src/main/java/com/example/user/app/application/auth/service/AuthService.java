package com.example.user.app.application.auth.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.common.exception.BaseException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.components.LoginComponent;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.dto.SecurityUserDetail;
import com.example.user.app.application.auth.dto.UsernamePassword;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import com.example.user.app.common.config.security.AccessTokenBlackListProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // repository
    private final RefreshTokenRepository refreshTokenRepository;

    // components
    private final LoginComponent loginComponent;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;                            // jwt component
    private final AccessTokenBlackListProvider accessTokenBlackListProvider;    // 블랙리스트 관리 컴포넌트

    /**
     * 토큰 발급 (username, password)
     * 유저명 유추(Username Enumeration) 취약점을 방지 하기 위해 로그인 응답시간 통일화
     */
    @Transactional
    public JwtTokenDto loginWithUsernameAndPassword(UsernamePassword usernamePassword, Device device) {

        // STEP 1: 유저 조회 시도
        Optional<SecurityUserDetail> maybeUser = loginComponent.loadByUsername(usernamePassword.username());
        SecurityUserDetail user = maybeUser.orElseGet(SecurityUserDetail::dummy);

        // STEP 2: password check는 항상 진행 (비용은 같음)
        boolean passwordMatch = user.validatePassword(passwordEncoder, usernamePassword.password());

        // STEP 4: 존재하고 비밀번호 확인
        if (maybeUser.isPresent() && passwordMatch) {
            // STEP 5: 상태검증
            user.validateStatus();

            loginComponent.loginSuccess(user);          // 로그인 성공 call back
            return jwtTokenProvider.get(user, device);  // 토큰 발급
        }

        // STEP 6: 존재하지 않거나 비밀번호가 틀리면 로그인 실패
        loginComponent.loginFail(user);
        throw new BaseException(AuthErrorCode.UNAUTHORIZED_BAD_CREDENTIALS);
    }

    /**
     * 토큰 발급 (refreshTokenVal)
     */
    @Transactional
    public JwtTokenDto refreshToken(String refreshTokenVal, Device requestDevice) {

        // STEP 1: 토큰 검증
        // 1. 토큰이 비어있으면 400 에러
        if (CommonUtil.isEmpty(refreshTokenVal)) {
            log.debug("[TOKEN ERROR] Refresh token is missing");
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_BAD_REQUEST_REFRESH_TOKEN);
        }

        // 2. 토큰 검증 실패 시 401 에러
        if (JwtUtil.invalidToken(refreshTokenVal)) {
            log.debug("[TOKEN ERROR] Refresh token({}) is invalid or expired", refreshTokenVal);
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
        }

        // 3. 토큰 저장소 조회 (없으면 401 에러)
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenValue(refreshTokenVal)
                .orElseThrow(() -> {
                    log.warn("[TOKEN ERROR] Refresh token({}) not found in repository", refreshTokenVal);
                    return new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
                });

        // STEP 2: 리프레쉬 토큰 디바이스 & 사용자 확인
        RefreshToken refreshToken = refreshTokenEntity.toDomain();
        if (!refreshToken.getDevice().isSameDevice(requestDevice)) {
            log.warn("[TOKEN ERROR] Refresh token({}) device mismatch. expected: {}, actual: {}",
                    refreshTokenVal, refreshToken.getDevice(), requestDevice);

            // 1. 디바이스 불일치 시 401 에러
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN_DEVICE_MISMATCH);
        }

        // 2. 유저 조회 (없으면 401 에러)
        SecurityUserDetail user = loginComponent.loadById(refreshToken.getUserId())
                .orElseThrow(() -> new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN));

        // 3. 유저 상태검증
        user.validateStatus();

        // STEP 3: 리프레쉬 토큰 마지막 사용일 업데이트
        refreshToken.used(Instant.now());
        refreshTokenRepository.save(RefreshTokenEntity.fromDomain(refreshToken));

        // STEP 4: 토큰 응답
        return jwtTokenProvider.get(user, requestDevice);  // 토큰 발급
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(UUID userId, String accessToken, String refreshToken) {

        // 1. 리프래쉬 토큰 삭제
        refreshTokenRepository.deleteByTokenValue(refreshToken);

        // 2. 액세스 토큰 블랙리스트에 추가 (redis 트랜잭션을 사용하여야함! 현재는 인메모리)
        accessTokenBlackListProvider.add(accessToken);

        // 3. 로그아웃 성공 로그
        log.info("[LOGOUT SUCCESS] User logged out. userId: {}, accessToken: {}", userId, accessToken);
    }

}
