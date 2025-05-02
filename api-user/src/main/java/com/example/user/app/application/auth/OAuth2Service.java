package com.example.user.app.application.auth;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.common.model.SecurityUser;
import com.example.user.app.application.auth.CustomAuthException.AuthErrorCode;
import com.example.user.app.application.token.JwtTokenProvider;
import com.example.user.app.domain.entity.User;
import com.example.user.app.domain.entity.UserRole;
import com.example.user.app.domain.entity.UserStatus;
import com.example.user.app.domain.repository.UserRepository;
import com.example.user.app.domain.service.NickNameTagGenerator;
import com.example.user.app.models.oauth2.OAuth2Response;
import com.example.user.app.request.OAuthRequest;
import com.example.user.app.response.JwtTokenResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    // repository
    private final UserRepository userRepository;

    private final Map<String, SocialOAuth2Service> socialServices;  // oauth2 component
    private final NickNameTagGenerator nickNameTagGenerator;        // nickName component
    private final JwtTokenProvider jwtTokenComponent;              // jwt component

    @Transactional
    public JwtTokenResponse createTokenByOAuth(@NonNull OAuthRequest oauthRequest) {
        log.info("createTokenByOAuth({}) 호출", oauthRequest);

        // 소셜 로그인 사용자 정보 추출
        OAuth2Response oauth2Response = getUserInfo(oauthRequest);
        log.debug("oauth response : {}", oauth2Response);

        // 사용자 정보 저장 혹은 업데이트
        User socialUser = saveOrUpdateUserAndGet(oauth2Response);

        // 사용자 정보기반 인증 객체 생성
        // SecurityUser securityUser = SecurityUser.of(socialUser);
        SecurityUser securityUser = new SecurityUser(
            socialUser.getId(),
            socialUser.getUsername(),
            socialUser.getPassword(),
            Collections.singletonList(socialUser.getRole().name())
        );

        // 토큰 발급
        JwtTokenResponse token = jwtTokenComponent.createToken(securityUser);
        log.info("[TOKEN SUCCESS] New token issued. userId: {}, accessToken: {}, refreshToken: {}", securityUser.id(), token.accessToken(), token.refreshToken());
        return token;
    }

    private OAuth2Response getUserInfo(OAuthRequest oauthRequest) {
        String provider = oauthRequest.provider().toLowerCase();

        return Optional.ofNullable(socialServices.get(provider))
                .map(service -> service.getUserInfo(oauthRequest))
                .orElseThrow(() -> new CustomAuthException(AuthErrorCode.OAUTH2_AUTH_FAILED, "지원하지 않는 소셜 로그인입니다."));
    }

    private User saveOrUpdateUserAndGet(@NonNull OAuth2Response oauth2Response) {
        User user = findOrCreateUser(oauth2Response);
        User saved = userRepository.save(user);

        return saved;
    }

    private User findOrCreateUser(OAuth2Response oauth2Response) {
        String username = oauth2Response.getProvider() + "_" + oauth2Response.getProviderId();

        return userRepository.findByUsername(username)
                .map(existingUser -> updateUserIfNeeded(existingUser, oauth2Response))
                .orElseGet(() -> createNewUser(username, oauth2Response));
    }

    private User updateUserIfNeeded(User user, OAuth2Response oauth2Response) {
        if (!Objects.equals(user.getNickName(), oauth2Response.getNickName())) {
            String nickName = oauth2Response.getNickName();
            String uniqueNickNameTag = nickNameTagGenerator.generateTag(nickName);
            user.changeNickName(nickName, uniqueNickNameTag);
        }

        return user;
    }

    private User createNewUser(String username, OAuth2Response oauth2Response) {
        String nickName = oauth2Response.getNickName();
        String uniqueNickNameTag = nickNameTagGenerator.generateTag(nickName);

        return User.builder()
                .username(username)
                .password(null)
                .name(oauth2Response.getName())
                .nickName(nickName)
                .nickNameTag(uniqueNickNameTag)
                .email(oauth2Response.getEmail())
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ENABLED)
                .build();
    }

}
