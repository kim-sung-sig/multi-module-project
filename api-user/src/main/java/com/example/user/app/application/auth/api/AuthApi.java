package com.example.user.app.application.auth.api;

import java.time.Duration;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.enums.CommonErrorCode;
import com.example.common.exception.BaseException;
import com.example.common.model.ApiResponse;
import com.example.common.model.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.dto.UsernamePassword;
import com.example.user.app.application.auth.dto.request.OAuthRequest;
import com.example.user.app.application.auth.dto.request.UserLoginRequest;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.auth.service.AuthService;
import com.example.user.app.application.auth.service.OAuth2Service;
import com.example.user.app.common.util.ApiResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApi {

    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    /**
     * 토큰 발급 with username and password
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody UserLoginRequest loginRequest) {

        log.debug("login request : {}", loginRequest);

        Device device = extractDeviceInfo(request);
        JwtTokenDto jwtTokenDto = authService.loginWithUsernameAndPassword(
                new UsernamePassword(loginRequest.username(), loginRequest.password()), device);
        log.debug("login response : {}", jwtTokenDto);

        setRefreshTokenCookie(response, jwtTokenDto.refreshToken());

        return ApiResponseUtil.ok(
                new JwtTokenResponse(jwtTokenDto.accessToken(), jwtTokenDto.accessTokenExpiry(), device));
    }

    /**
     * 토큰 발급 with social
     */
    @PostMapping("/oauth/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> oauthLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid @RequestBody OAuthRequest oAuthRequest) {

        log.debug("oauth login request : {}", oAuthRequest);

        Device device = extractDeviceInfo(request);
        JwtTokenDto token = oAuth2Service.createTokenByOAuth(oAuthRequest, device);
        log.debug("oauth login response : {}", token);

        setRefreshTokenCookie(response, token.refreshToken());

        return ApiResponseUtil.ok(
                new JwtTokenResponse(token.accessToken(), token.accessTokenExpiry(), device));
    }

    /**
     * 토큰 발급 with refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken) {
        log.debug("refresh request : {}", refreshToken);

        Device device = extractDeviceInfo(request);
        JwtTokenDto token = authService.refreshToken(refreshToken, device);
        log.debug("refresh response : {}", token);

        setRefreshTokenCookie(response, token.refreshToken());

        return ApiResponseUtil.ok(
                new JwtTokenResponse(token.accessToken(), token.accessTokenExpiry(), device));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletResponse response,
            @CookieValue(value = JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (CommonUtil.isEmpty(authentication) || !authentication.isAuthenticated() || isAnonymous(authentication)) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED, "인증되지 않은 요청입니다.");
        }

        Object principalObj = authentication.getPrincipal();
        Object credentialsObj = authentication.getCredentials();

        // principal 타입 검사
        if (!(principalObj instanceof SecurityUser)) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED, "잘못된 사용자 정보입니다.");
        }

        // credentials 타입 검사 (예: JWT 토큰)
        if (!(credentialsObj instanceof String)) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED, "유효하지 않은 인증 정보입니다.");
        }

        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        authService.logout(principal.getId(), credentials, refreshToken);
        removeRefreshTokenCookie(response);

        return ApiResponseUtil.ok();
    }

    // 리프레쉬 토큰을 cookie에 저장
    private static void setRefreshTokenCookie(
            @NonNull HttpServletResponse response,
            @NonNull RefreshToken refreshToken) {

        long maxAge = Duration.between(Instant.now(), refreshToken.getExpiryAt())
                .toSeconds();

        ResponseCookie cookie = ResponseCookie
                .from(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getTokenValue())
                .httpOnly(true)
                // .secure(true) // HTTPS 환경에서만 전달되도록
                .path("/") // 요청 범위 제한
                .sameSite("Strict")
                .maxAge(maxAge) // 유효기간 설정 (optional)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private static void removeRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie
                .from(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/") // 요청 범위 제한
                .sameSite("Strict")
                .maxAge(0) // 만료 설정
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private Device extractDeviceInfo(HttpServletRequest request) {
        String deviceId = request.getHeader("X-Device-Id");
        String platform = request.getHeader("X-Platform");
        String browser = request.getHeader("X-Browser");

        if (deviceId == null || platform == null || browser == null) {
            throw new BaseException(CommonErrorCode.INVALID_INPUT_REQUEST, "Device 정보 헤더가 누락되었습니다.");
        }

        return new Device(deviceId, platform, browser);
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication.getPrincipal() instanceof String &&
                "anonymousUser".equals(authentication.getPrincipal());
    }

}
