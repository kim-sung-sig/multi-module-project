package com.example.user.app.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.common.domain.audit.AuditorProvider;
import com.example.common.util.CommonUtil;

@Component
public class AuditorProviderImpl implements AuditorProvider {

    @Override
    public String getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (CommonUtil.isEmpty(auth) || !auth.isAuthenticated()) return "SYSTEM";
        return auth.getName();
    }

}
