package com.example.user.app.application.auth.dto;

import com.example.common.model.SecurityUser;
import com.example.user.app.application.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;

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

}
