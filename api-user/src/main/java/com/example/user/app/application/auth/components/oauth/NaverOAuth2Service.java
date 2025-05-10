package com.example.user.app.application.auth.components.oauth;

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
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("naver")
@RequiredArgsConstructor
public class NaverOAuth2Service implements SocialOAuth2Service {

    private final RestClient restClient = RestClient.create();

    @Value("${oauth.naver.client-id}")
    private String naverClientId;

    @Value("${oauth.naver.client-secret}")
    private String naverClientSecret;

    @PostConstruct
    public void init() {
        log.debug("naverClientId: " + naverClientId);
        log.debug("naverClientSecret: " + naverClientSecret);
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
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("code", oauthRequest.code());
        params.add("state", oauthRequest.state());

        ResponseEntity<Map<String, Object>> response = restClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(params)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    log.error("4xx error during(get access token) request to Naver. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
                    throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get access token");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    log.error("5xx error during(get access token) request to Naver. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
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
                .uri("https://openapi.naver.com/v1/nid/me")
                .header(HttpHeaders.AUTHORIZATION, JwtUtil.BEARER_PREFIX + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    log.error("4xx error during(get userInfo) request to Naver. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
                    throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get userInfo");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    log.error("5xx error during(get userInfo) request to Naver. Request: {}, Response status: {}, Response: {}", req, res.getStatusCode(), res);
                    throw new TemporaryException(5);
                })
                .toEntity(new ParameterizedTypeReference<>() {});

        Map<String, Object> body = response.getBody();

        if (body == null) throw new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "Failed to get user info");

        return new NaverOAuth2Data(body);
    }

}

@ToString
class NaverOAuth2Data implements OAuth2Data {

    private final Map<String, Object> attribute;

    @SuppressWarnings("unchecked")
    public NaverOAuth2Data(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute.getOrDefault("id", "").toString();
    }

    @Override
    public String getEmail() {
        return attribute.getOrDefault("email", "").toString();
    }

    @Override
    public String getName() {
        return attribute.getOrDefault("name", "").toString();
    }

    @Override
    public String getNickName() {
        return attribute.getOrDefault("nickname", "").toString();
    }
}