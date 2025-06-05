package com.example.user.app.application.auth.components.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    // GOOGLE("google"),
    // APPLE("apple"),
    ;

    private final String type;

}
