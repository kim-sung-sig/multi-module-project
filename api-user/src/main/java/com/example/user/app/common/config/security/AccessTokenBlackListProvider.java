package com.example.user.app.common.config.security;

public interface AccessTokenBlackListProvider {

    void add(String accessToken);

    boolean isBlack(String accessToken);

}
