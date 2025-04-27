package com.example.common.security.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

public record SecurityUser(
    UUID id,
    String username,
    String password,
    UserRole role,
    Collection<? extends GrantedAuthority> authorities
) implements Serializable {

    private static final long serialVersionUID = 1L;

    public SecurityUser(JwtUserInfo jwtUserInfo) {
        this(jwtUserInfo.id(), jwtUserInfo.username(), "", jwtUserInfo.role(), setAuthorities(jwtUserInfo.role()));
    }

    private static Collection<? extends GrantedAuthority> setAuthorities(UserRole role) {
        Assert.notNull(role, "role must not be null");
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

}
