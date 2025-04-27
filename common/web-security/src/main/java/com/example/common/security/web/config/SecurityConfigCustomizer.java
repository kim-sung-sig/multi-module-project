package com.example.common.security.web.config;

import org.springframework.security.web.SecurityFilterChain;

public interface SecurityConfigCustomizer {
    void customize(SecurityFilterChain http) throws Exception;
}
