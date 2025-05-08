package com.example.user.app.application.service.auth;

import com.example.user.app.dto.request.OAuthRequest;
import com.example.user.app.models.oauth2.OAuth2Response;

public interface SocialOAuth2Service {

    OAuth2Response getUserInfo(OAuthRequest oauthRequest);

    String getAccessToken(OAuthRequest oauthRequest);

    OAuth2Response getUserInfo(String accessToken);

}
