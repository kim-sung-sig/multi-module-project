package com.example.user.app.application.auth.api;

import com.example.common.enums.ErrorCode;
import com.example.common.exception.BusinessException;
import com.example.common.model.ApiResponse;
import com.example.common.model.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.user.app.application.auth.dto.request.OAuthRequest;
import com.example.user.app.application.auth.dto.request.TokenRefreshRequest;
import com.example.user.app.application.auth.dto.request.UserLoginRequest;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.service.AuthService;
import com.example.user.app.application.auth.service.OAuth2Service;
import com.example.user.app.common.util.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse< JwtTokenResponse >> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        log.debug("login request : {}", loginRequest);

        JwtTokenResponse token = authService.loginWithUsernameAndPassword(loginRequest.username(), loginRequest.password());
        log.debug("login response : {}", token);

        return ApiResponseUtil.ok(token);
    }

    /**
     * 토큰 발급 with social
     */
    @PostMapping("/oauth/login")
    public ResponseEntity<ApiResponse< JwtTokenResponse >> oauthLogin(@Valid @RequestBody OAuthRequest oauthRequest) {
        log.debug("oauth login request : {}", oauthRequest);

        JwtTokenResponse token = oAuth2Service.createTokenByOAuth(oauthRequest);
        log.debug("oauth login response : {}", token);

        return ApiResponseUtil.ok(token);
    }

    /**
     * 토큰 발급 with refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse< JwtTokenResponse >> refresh(@Valid @RequestBody TokenRefreshRequest refreshRequest) {
        log.debug("refresh request : {}", refreshRequest);

        JwtTokenResponse token = authService.reissueToken(refreshRequest.refreshToken());
        log.debug("refresh response : {}", token);

        return ApiResponseUtil.ok(token);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse< Void >> logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (CommonUtil.isEmpty(authentication) || !authentication.isAuthenticated() || isAnonymous(authentication)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 요청입니다.");
        }

        Object principalObj = authentication.getPrincipal();
        Object credentialsObj = authentication.getCredentials();

        // principal 타입 검사
        if (!(principalObj instanceof SecurityUser)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "잘못된 사용자 정보입니다.");
        }

        // credentials 타입 검사 (예: JWT 토큰)
        if (!(credentialsObj instanceof String)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "유효하지 않은 인증 정보입니다.");
        }

        SecurityUser principal = (SecurityUser) authentication.getPrincipal();
        String credentials = (String) authentication.getCredentials();

        authService.logout(principal.getId(), credentials);

        return ApiResponseUtil.ok();
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication.getPrincipal() instanceof String &&
                "anonymousUser".equals(authentication.getPrincipal());
    }

}
