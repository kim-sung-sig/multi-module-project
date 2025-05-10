package com.example.user.app.application.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.model.ApiResponse;
import com.example.user.app.application.auth.dto.request.OAuthRequest;
import com.example.user.app.application.auth.dto.request.UserLoginRequest;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import com.example.user.app.application.auth.service.AuthService;
import com.example.user.app.application.auth.service.OAuth2Service;
import com.example.user.app.common.util.ApiResponseUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginApi {

    private final AuthService authService;
    private final OAuth2Service oAuth2Service;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse< JwtTokenResponse >> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        log.debug("login request : {}", loginRequest);

        JwtTokenResponse token = authService.createTokenByUsernameAndPassword(loginRequest.username(), loginRequest.password());
        log.debug("login response : {}", token);

        return ApiResponseUtil.ok(token);
    }

    // 토큰 발급 with (소셜로그인)
    @PostMapping("/oauth/login")
    public ResponseEntity<ApiResponse<JwtTokenResponse>> oauthLogin(@Valid @RequestBody OAuthRequest oauthRequest) {
        log.debug("oauth login request : {}", oauthRequest);

        JwtTokenResponse token = oAuth2Service.createTokenByOAuth(oauthRequest);
        log.debug("oauth login response : {}", token);

        return ApiResponseUtil.ok(token);
    }

}
