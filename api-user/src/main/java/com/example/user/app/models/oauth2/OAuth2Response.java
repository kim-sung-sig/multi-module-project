package com.example.user.app.models.oauth2;

public interface OAuth2Response {

    String getProvider(); //제공자 (Ex. naver, google, ...)

    String getProviderId(); //제공자에서 발급해주는 아이디(번호)

    String getEmail(); //이메일

    String getName(); //사용자 실명 (설정한 이름)

    String getNickName(); //사용자 닉네임

}
