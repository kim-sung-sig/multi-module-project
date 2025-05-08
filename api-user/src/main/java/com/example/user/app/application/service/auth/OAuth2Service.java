package com.example.user.app.application.service.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.common.model.SecurityUser;
import com.example.user.app.application.service.auth.CustomAuthException.AuthErrorCode;
import com.example.user.app.application.service.nickname.NickNameTagGenerator;
import com.example.user.app.application.service.token.JwtTokenProvider;
import com.example.user.app.dto.request.OAuthRequest;
import com.example.user.app.dto.response.JwtTokenResponse;
import com.example.user.app.infra.entity.User;
import com.example.user.app.infra.entity.UserRole;
import com.example.user.app.infra.entity.UserStatus;
import com.example.user.app.infra.repository.UserRepository;
import com.example.user.app.models.oauth2.OAuth2Response;
import com.example.user.app.vo.NickName;
import com.example.user.app.vo.NickNameTag;

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
    private final JwtTokenProvider jwtTokenProvider;              // jwt component

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
        JwtTokenResponse token = jwtTokenProvider.getTokenResponseWithDeletion(securityUser);
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
            NickName.tryCreate(nickName)
                    .ifPresent(n -> {
                        NickNameTag uniqueNickNameTag = nickNameTagGenerator.generateTag(n);
                        user.changeNickName(uniqueNickNameTag);
                    });
        }

        return user;
    }

    private User createNewUser(String username, OAuth2Response oauth2Response) {
        String nickName = oauth2Response.getNickName();
        NickName validNickName = NickName.tryCreate(nickName)
                .orElse(new NickName(generateRandomNickname()));

        NickNameTag uniqueNickNameTag = nickNameTagGenerator.generateTag(validNickName);

        return User.builder()
                .username(username)
                .password(null)
                .name(oauth2Response.getName())
                .nickName(uniqueNickNameTag.getNickName().get())
                .nickNameTag(uniqueNickNameTag.getTag())
                .email(oauth2Response.getEmail())
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ENABLED)
                .build();
    }

    private static final Map<String, List<String>> VERB_ADJECTIVE_MAP = Map.of(
        "자고 있는", List.of("편안한", "평화로운", "귀여운"),
        "뛰어노는", List.of("상큼한", "신나는", "재미있는"),
        "춤추는", List.of("멋진", "귀여운", "신나는"),
        "하늘을 나는", List.of("자유로운", "멋진", "대담한"),
        "산책하는", List.of("행복한", "여유로운", "상쾌한")
    );

    private static final List<String> VERBS = new ArrayList<>(VERB_ADJECTIVE_MAP.keySet());

    private static final List<String> ANIMALS = List.of(
        "강아지", "고양이", "토끼", "펭귄", "다람쥐", "햄스터", "사슴", "고슴도치"
    );

    private static String generateRandomNickname() {
        Random random = new Random();

        String verb = VERBS.get(random.nextInt(VERBS.size()));
        List<String> adjectives = VERB_ADJECTIVE_MAP.get(verb);
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String animal = ANIMALS.get(random.nextInt(ANIMALS.size()));

        return verb + " " + adjective + " " + animal;
    }

}
