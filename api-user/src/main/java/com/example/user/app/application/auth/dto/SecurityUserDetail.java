package com.example.user.app.application.auth.dto;

import com.example.common.exception.BaseException;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.user.entity.User;
import com.example.user.app.application.user.entity.UserRole;
import com.example.user.app.application.user.entity.UserStatus;
import com.example.user.app.common.dto.security.SecurityUser;
import com.example.user.app.common.util.PasswordUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SecurityUserDetail extends SecurityUser {

    private final boolean enabled;
    private final boolean accountNonLocked;

    public SecurityUserDetail(User user) {
        super(user.getId(), user.getUsername(), user.getPassword(), Collections.singletonList(user.getRole().name()));

        switch (user.getStatus()) {
            case ENABLED -> {
                enabled = true;
                accountNonLocked = true;
            }
            case LOCKED -> {
                enabled = true;
                accountNonLocked = false;
            }
            default -> {
                enabled = false;
                accountNonLocked = false;
            }
        }

    }

    public static SecurityUserDetail dummy() {
        DummyUser dummyUser = new DummyUser();
        dummyUser.setId(UUID.randomUUID());
        dummyUser.setUsername("dummy");
        dummyUser.setPassword(PasswordUtils.encode("dummy")); // 비밀번호 보안정책상 5글자는 허용안됨
        dummyUser.setRole(UserRole.ROLE_USER);
        dummyUser.setStatus(UserStatus.ENABLED);
        dummyUser.setDummy(true);

        return new SecurityUserDetail(dummyUser);
    }

    public DummyUser toUser() {
        DummyUser dummyUser = new DummyUser();
        dummyUser.setId(this.getId());
        dummyUser.setUsername(this.getUsername());
        dummyUser.setPassword(this.getPassword());
        dummyUser.setDummy(true);
        return dummyUser;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DummyUser extends User {
        boolean isDummy = true;
    }

    public void validateStatus() {
        if (!this.isEnabled()) {
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_EXPIRED);
        }

        if (!this.isAccountNonLocked()) {
            throw new BaseException(AuthErrorCode.FORBIDDEN_LOCKED);
        }
    }
}
