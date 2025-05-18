package com.example.user.app.application.auth.api;

import com.example.common.exception.BaseException;
import com.example.common.model.ApiResponse;
import com.example.common.model.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.domain.RefreshToken;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.dto.UsernamePassword;
import com.example.user.app.application.auth.dto.request.OAuthRequestWrapper;
import com.example.user.app.application.auth.dto.request.UserLoginRequest;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.service.AuthService;
import com.example.user.app.application.auth.service.OAuth2Service;
import com.example.user.app.common.enums.AuthErrorCode;
import com.example.user.app.common.util.ApiResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

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
    public ResponseEntity<ApiResponse<JwtTokenResponse>> login(HttpServletResponse response, @Valid @RequestBody UserLoginRequest loginRequest) {
        log.debug("login request : {}", loginRequest);

        JwtTokenDto token = authService.loginWithUsernameAndPassword(new UsernamePassword(loginRequest.username(), loginRequest.password()), new Device());
        log.debug("login response : {}", token);

        setRefreshTokenCookie(response, token.refreshToken());
        return ApiResponseUtil.ok(new JwtTokenResponse(token.accessToken()));
    }

    /**
     * 토큰 발급 with social
     */
    @PostMapping("/oauth/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> oauthLogin(HttpServletResponse response, @Valid @RequestBody OAuthRequestWrapper oauthRequestWrapper) {
        log.debug("oauth login request : {}", oauthRequestWrapper);

        JwtTokenDto token = oAuth2Service.createTokenByOAuth(oauthRequestWrapper.oAuth(), oauthRequestWrapper.device());
        log.debug("oauth login response : {}", token);

        setRefreshTokenCookie(response, token.refreshToken());
        return ApiResponseUtil.ok(new JwtTokenResponse(token.accessToken()));
    }

    /**
     * 토큰 발급 with refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> refresh(
            HttpServletResponse response,
            @CookieValue(value = JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken
    ) {
        log.debug("refresh request : {}", refreshToken);

        JwtTokenDto token = authService.reissueToken(refreshToken, new Device());
        log.debug("refresh response : {}", token);

        setRefreshTokenCookie(response, token.refreshToken());
        return ApiResponseUtil.ok(new JwtTokenResponse(token.accessToken()));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse< Void >> logout() {
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

        authService.logout(principal.getId(), credentials);

        return ApiResponseUtil.ok();
    }

    // 리프레쉬 토큰을 cookie에 저장
    private static void setRefreshTokenCookie(@NonNull HttpServletResponse response, @NonNull RefreshToken refreshToken) {

        long now = Instant.now().getEpochSecond();
        long exp = refreshToken.getExpiryAt().getEpochSecond();

        ResponseCookie cookie = ResponseCookie.from(JwtTokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken.getTokenValue())
                .httpOnly(true)
                // .secure(true) // HTTPS 환경에서만 전달되도록
                .path("/") // 요청 범위 제한
                .sameSite("Strict")
                .maxAge(Duration.ofSeconds(Math.max(exp - now, 0))) // 유효기간 설정 (optional)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication.getPrincipal() instanceof String &&
                "anonymousUser".equals(authentication.getPrincipal());
    }

}
