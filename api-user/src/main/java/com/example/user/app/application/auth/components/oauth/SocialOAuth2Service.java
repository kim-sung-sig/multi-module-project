package com.example.user.app.application.auth.components.oauth;

import com.example.user.app.application.auth.dto.request.OAuthRequest;

public interface SocialOAuth2Service {

    SocialType getSocialType();

    OAuth2Data getUserInfo(OAuthRequest oauthRequest) throws OAuth2Exception;

    String getAccessToken(OAuthRequest oauthRequest) throws OAuth2Exception;

    OAuth2Data getUserInfo(String accessToken) throws OAuth2Exception;

}
