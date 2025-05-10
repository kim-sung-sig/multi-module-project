package com.example.user.app.application.auth.components.oauth;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.example.common.exception.TemporaryException;
import com.example.common.util.JwtUtil;
import com.example.user.app.application.auth.CustomAuthException;
import com.example.user.app.application.auth.CustomAuthException.AuthErrorCode;
import com.example.user.app.application.auth.components.SocialOAuth2Service;
import com.example.user.app.application.auth.dto.OAuth2Data;
import com.example.user.app.application.auth.dto.request.OAuthRequest;

import jakarta.annotation.PostConstruct;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("kakao")
public class KakaoOAuth2Service implements SocialOAuth2Service {

    private final RestClient restClient = RestClient.create();

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret}")
    private String kakaoClientSecret;

    @PostConstruct
    public void init() {
        log.debug("kakaoClientId: " + kakaoClientId);
        log.debug("kakaoClientSecret: " + kakaoClientSecret);
    }

    @Override
    public OAuth2Data getUserInfo(OAuthRequest oauthRequest) {
        String accessToken = this.getAccessToken(oauthRequest);
        return this.getUserInfo(accessToken);
    }

    @Override
    public String getAccessToken(OAuthRequest oauthRequest) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("client_secret", kakaoClientSecret);
        params.add("code", oauthRequest.code());
        params.add("state", oauthRequest.state());

        ResponseEntity<Map<String, Object>> response = restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8).toString())
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    log.error("4xx error during(get access token) request to Kakao. Request: {}, Response status: {}", req, res.getStatusCode());
                    throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get access token");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    log.error("5xx error during(get access token) request to Kakao. Request: {}, Response status: {}", req, res.getStatusCode());
                    throw new TemporaryException(5);
                })
                .toEntity(new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();

        if (body == null) throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get access token");

        String accessToken = (String) body.get("access_token");
        return accessToken;
    }

    @Override
    public OAuth2Data getUserInfo(String accessToken) {
        ResponseEntity<Map<String, Object>> response = restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, JwtUtil.BEARER_PREFIX + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8).toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    log.error("4xx error during(get userInfo) request to Kakao. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
                    throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get userInfo");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    log.error("5xx error during(get userInfo) request to Kakao. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
                    throw new TemporaryException(5);
                })
                .toEntity(new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();

        if (body == null) throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get userInfo");

        return new KakaoOAuth2Data(body);
    }

}

@ToString
class KakaoOAuth2Data implements OAuth2Data {

    private final Map<String, Object> attribute;

    public KakaoOAuth2Data(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attribute.getOrDefault("id", ""));
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attribute.getOrDefault("properties", Collections.emptyMap());
        return String.valueOf(properties.getOrDefault("nickname", ""));
    }

    @Override
    public String getNickName() {
        @SuppressWarnings("unchecked")
        Map<String, Object> properties = (Map<String, Object>) attribute.getOrDefault("properties", Collections.emptyMap());
        return String.valueOf(properties.getOrDefault("nickname", ""));
    }

}