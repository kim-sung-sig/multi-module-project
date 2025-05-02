package com.example.user.app.models.oauth2;

import java.util.Collections;
import java.util.Map;

import lombok.ToString;

@ToString
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    public KakaoResponse(Map<String, Object> attribute) {
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
