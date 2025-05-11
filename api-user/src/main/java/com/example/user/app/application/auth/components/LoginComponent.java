package com.example.user.app.application.auth.components;

import com.example.common.model.SecurityUser;
import com.example.common.util.EventPublisher;
import com.example.user.app.application.auth.dto.SecurityUserDetail;
import com.example.user.app.application.user.entity.User;
import com.example.user.app.application.user.entity.UserStatus;
import com.example.user.app.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginComponent {

    private final UserRepository userRepository;

    public Optional<SecurityUserDetail> loadById(UUID userId) {
        return userRepository.findById(userId)
                .map(SecurityUserDetail::new);
    }

    public Optional<SecurityUserDetail> loadByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(SecurityUserDetail::new);
    }

    /**
     * 로그인 성공시 호출
     */
    @Transactional
    public void loginSuccess(SecurityUser securityUser) {
        Optional<User> userOpt = userRepository.findById(securityUser.getId());

        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        user.setLastLoginAt(Instant.now());
        user.setLoginFailCount(0);
        userRepository.save(user);

        EventPublisher.publish(null); // TODO 유저 로그인 이벤트
    }

    /**
     * 로그인 실패시 호출
     */
    @Transactional
    public void loginFail(SecurityUser securityUser) {
        Optional<User> userOpt = userRepository.findById(securityUser.getId());

        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        user.setLoginFailCount(user.getLoginFailCount() + 1);
        if (user.getLoginFailCount() > 30) user.setStatus(UserStatus.LOCKED);
        userRepository.save(user);
    }

}
