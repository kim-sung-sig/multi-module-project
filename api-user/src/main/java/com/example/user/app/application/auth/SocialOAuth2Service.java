package com.example.user.app.application.auth;

import com.example.user.app.models.oauth2.OAuth2Response;
import com.example.user.app.request.OAuthRequest;

public interface SocialOAuth2Service {

    OAuth2Response getUserInfo(OAuthRequest oauthRequest);

    String getAccessToken(OAuthRequest oauthRequest);

    OAuth2Response getUserInfo(String accessToken);

}
