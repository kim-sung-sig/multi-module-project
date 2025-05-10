package com.example.user.app.application.auth.components;

import com.example.user.app.application.auth.dto.OAuth2Data;
import com.example.user.app.application.auth.dto.request.OAuthRequest;

public interface SocialOAuth2Service {

    OAuth2Data getUserInfo(OAuthRequest oauthRequest);

    String getAccessToken(OAuthRequest oauthRequest);

    OAuth2Data getUserInfo(String accessToken);

}
