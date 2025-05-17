package com.example.user.app.application.auth.service;

import com.example.common.enums.ErrorCode;
import com.example.common.exception.BaseException;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.components.LoginComponent;
import com.example.user.app.application.auth.dto.SecurityUserDetail;
import com.example.user.app.application.auth.dto.UsernamePassword;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.entity.Device;
import com.example.user.app.application.auth.entity.RefreshTokenEntity;
import com.example.user.app.application.auth.repository.RefreshTokenRepository;
import com.example.user.app.common.config.security.AccessTokenBlackListProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AccessTokenBlackListProvider accessTokenBlackListProvider;

    /**
     * 토큰 발급 (username, password)
     * 유저명 유추(Username Enumeration) 취약점을 방지 하기 위해 로그인 응답시간 통일화
     */
    @Transactional
    public JwtTokenResponse loginWithUsernameAndPassword(UsernamePassword usernamePassword, Device device) {

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
        throw new BaseException(ErrorCode.UNAUTHORIZED_BAD_CREDENTIALS);
    }

    /**
     * 토큰 발급 (refreshToken)
     */
    @Transactional
    public JwtTokenResponse reissueToken(String refreshToken, Device device) {
        final var exception = new BaseException(ErrorCode.UNAUTHORIZED, "Refresh token invalid or expired");

        // 1. 토큰이 비어있으면 400 에러
        if (CommonUtil.isEmpty(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token is missing");
            throw new BaseException(ErrorCode.INVALID_INPUT_REQUEST, "Refresh token is missing");
        }

        // 2. 토큰 검증 실패 시 401 에러
        if (JwtUtil.invalidToken(refreshToken)) {
            log.debug("[TOKEN ERROR] Refresh token({}) is invalid or expired", refreshToken);
            throw new BaseException(ErrorCode.UNAUTHORIZED_INVALID_REFRESH_TOKEN);
        }

        // 3. 토큰 저장소 조회 (없으면 401 에러)
        UUID userId = refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(RefreshTokenEntity::getUserId)
                .orElseThrow(() -> {
                    log.warn("[TOKEN ERROR] Refresh token({}) not found in repository", refreshToken);
                    return new BaseException(ErrorCode.UNAUTHORIZED_INVALID_REFRESH_TOKEN);
                });

        // 4. 유저 조회 (없으면 401 에러)
        var securityUser = loginComponent.loadById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.UNAUTHORIZED));

        // 5. 유저 상태검증
        checkUserStatus(securityUser);

        // 6. 새로운 토큰 발급
        return jwtTokenProvider.getTokenResponseWithContinuous(securityUser);
    }

    private void checkUserStatus(SecurityUserDetail securityUser) {
        if (!securityUser.isEnabled()) {
            log.warn("[SECURITY WARNING] Disabled user attempted to log in. userId: {}", securityUser.getId());
            throw new BaseException(ErrorCode.UNAUTHORIZED_BAD_CREDENTIALS);
        }

        if (!securityUser.isAccountNonLocked()) {
            log.warn("[SECURITY WARNING] Locked user attempted to log in. userId: {}", securityUser.getId());
            throw new BaseException(ErrorCode.UNAUTHORIZED_LOCKED);
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
