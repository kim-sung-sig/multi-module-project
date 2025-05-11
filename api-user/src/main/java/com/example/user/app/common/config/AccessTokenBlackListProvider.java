package com.example.user.app.common.config;

public interface AccessTokenBlackListProvider {

    void add(String accessToken);

    boolean isBlack(String accessToken);

}
