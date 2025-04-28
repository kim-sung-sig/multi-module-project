package com.example.user.auth.config;

import org.springframework.stereotype.Component;

import com.example.common.security.web.config.WhiteListProviderV1;

@Component
public class WhiteListProviderV1Imple implements WhiteListProviderV1 {

    @Override
    public String[] getWhiteList() {
        return new String[]{
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/actuator/**",
                "/api/user/auth/login",
                "/api/user/auth/oauth2/login",
                "/api/user/auth/token/refresh",
                "/api/user/auth/token/refresh/verify",
        };
    }

}
