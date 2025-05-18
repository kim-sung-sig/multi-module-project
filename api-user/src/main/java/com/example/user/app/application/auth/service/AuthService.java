package com.example.user.app.application.auth.service;

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
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import com.example.user.app.common.enums.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    // repository
    private final RefreshTokenRepository refreshTokenRepository;

    private final LoginComponent loginComponent;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;                            // jwt component

    /**
     * 토큰 발급 (username, password)
     * 유저명 유추(Username Enumeration) 취약점을 방지 하기 위해 로그인 응답시간 통일화
     */
    @Transactional
    public JwtTokenDto loginWithUsernameAndPassword(UsernamePassword usernamePassword, Device device) {

        // STEP 1: 유저 조회 시도
        Optional<SecurityUserDetail> maybeUser = loginComponent.loadByUsername(usernamePassword.username());

        // STEP 2: 존재하든 아니든 더미 유저로 통일 처리
        SecurityUserDetail user = maybeUser.orElseGet(SecurityUserDetail::dummy);

        // STEP 3: 상태검증 (더미는 항상 통과)
        checkUserStatus(user);

        // STEP 4: password check는 항상 진행 (비용은 같음)
        boolean passwordMatch = passwordEncoder.matches(usernamePassword.password(), user.getPassword());

        // STEP 5: 존재하고 비밀번호도 맞으면 로그인 성공
        if (maybeUser.isPresent() && passwordMatch) {
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
    public JwtTokenDto reissueToken(String refreshTokenVal, Device device) {

        // STEP 1: 토큰 검증
        if (CommonUtil.isEmpty(refreshTokenVal)) {
            log.debug("[TOKEN ERROR] Refresh token is missing");
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_BAD_REQUEST_REFRESH_TOKEN);                      // 1. 토큰이 비어있으면 400 에러
        }

        if (JwtUtil.invalidToken(refreshTokenVal)) {
            log.debug("[TOKEN ERROR] Refresh token({}) is invalid or expired", refreshTokenVal);
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);                                  // 2. 토큰 검증 실패 시 401 에러
        }

        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByTokenValue(refreshTokenVal)
                .orElseThrow(() -> {
                    log.warn("[TOKEN ERROR] Refresh token({}) not found in repository", refreshTokenVal);
                    return new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);                         // 3. 토큰 저장소 조회 (없으면 401 에러)
                });

        // STEP 2: 리프레쉬 토큰 디바이스 & 사용자 확인
        RefreshToken refreshToken = RefreshTokenEntity.toDomain(refreshTokenEntity);
        if (!refreshToken.isSameDevice(device)) {
            log.warn("[TOKEN ERROR] Refresh token({}) device mismatch. expected: {}, actual: {}",
                    refreshTokenVal, refreshToken.getDevice(), device);
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN_DEVICE_MISMATCH);                  // 1. 디바이스 불일치 시 401 에러
        }

        SecurityUserDetail user = loginComponent.loadById(refreshToken.getUserId())
                .orElseThrow(() -> new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN));                // 2. 유저 조회 (없으면 401 에러)
        checkUserStatus(user);                                                                                  // 3. 유저 상태검증

        // STEP 3: 리프레쉬 토큰 마지막 사용일 업데이트
        refreshToken.used(Instant.now());                                                                       // 1. 마지막 사용일 업데이트
        refreshTokenRepository.save(RefreshTokenEntity.fromDomain(refreshToken));                               // 2. 리프레쉬 토큰 업데이트

        // STEP 4: 토큰 응답
        return jwtTokenProvider.get(user, device);  // 토큰 발급
    }

    private void checkUserStatus(SecurityUserDetail securityUser) {
        if (!securityUser.isEnabled()) {
            log.warn("[SECURITY WARNING] Disabled user attempted to log in. userId: {}", securityUser.getId());
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_EXPIRED);
        }

        if (!securityUser.isAccountNonLocked()) {
            log.warn("[SECURITY WARNING] Locked user attempted to log in. userId: {}", securityUser.getId());
            throw new BaseException(AuthErrorCode.FORBIDDEN_LOCKED);
        }
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(UUID userId, String accessToken) {
        // 1. 리프래쉬 토큰 삭제
        refreshTokenRepository.deleteByUserId(userId);

        // 3. 로그아웃 성공 로그
        log.info("[LOGOUT SUCCESS] User logged out. userId: {}, accessToken: {}", userId, accessToken);
    }

}
