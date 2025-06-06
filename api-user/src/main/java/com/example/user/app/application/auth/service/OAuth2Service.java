package com.example.user.app.application.auth.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.common.enums.CommonErrorCode;
import com.example.common.exception.BaseException;
import com.example.common.exception.TemporaryException;
import com.example.user.app.application.auth.components.JwtTokenProvider;
import com.example.user.app.application.auth.components.oauth.OAuth2Data;
import com.example.user.app.application.auth.components.oauth.OAuth2Exception;
import com.example.user.app.application.auth.components.oauth.SocialOAuth2Service;
import com.example.user.app.application.auth.components.oauth.SocialType;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.dto.JwtTokenDto;
import com.example.user.app.application.auth.dto.request.OAuthRequest;
import com.example.user.app.application.nickname.domain.NickName;
import com.example.user.app.application.nickname.domain.NickNameTag;
import com.example.user.app.application.nickname.service.NickNameTagGenerator;
import com.example.user.app.application.user.entity.User;
import com.example.user.app.application.user.entity.UserRole;
import com.example.user.app.application.user.entity.UserStatus;
import com.example.user.app.application.user.repository.UserRepository;
import com.example.user.app.common.dto.security.SecurityUser;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuth2Service {

    // repository
    private final UserRepository userRepository;

    // components
    private final JwtTokenProvider jwtTokenProvider;                // jwt component
    private final NickNameTagGenerator nickNameTagGenerator;        // nickName component
    private final Map<SocialType, SocialOAuth2Service> socialServices;  // oauth2 component

    public OAuth2Service(
        UserRepository userRepository,
        JwtTokenProvider jwtTokenProvider,
        NickNameTagGenerator nickNameTagGenerator,
        List<SocialOAuth2Service> socialServices
    ) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.nickNameTagGenerator = nickNameTagGenerator;
        this.socialServices = socialServices.stream()
                .collect(Collectors.toMap(SocialOAuth2Service::getSocialType, Function.identity()));
    }

    @Transactional
    public JwtTokenDto createTokenByOAuth(OAuthRequest oauthRequest, Device device) {
        log.info("createTokenByOAuth({}) 호출", oauthRequest);

        // 소셜 로그인 사용자 정보 추출
        OAuth2Data oauth2Data = getUserInfo(oauthRequest);
        log.debug("oauth response : {}", oauth2Data);

        // 사용자 정보 저장 혹은 업데이트
        User socialUser = saveOrUpdateUserAndGet(oauth2Data);

        // 사용자 정보기반 인증 객체 생성
        // SecurityUser securityUser = SecurityUser.of(socialUser);
        SecurityUser securityUser = new SecurityUser(
            socialUser.getId(),
            socialUser.getUsername(),
            socialUser.getPassword(),
            Collections.singletonList(socialUser.getRole().name())
        );

        // 토큰 발급
        return jwtTokenProvider.get(securityUser, device);
    }

    private OAuth2Data getUserInfo(OAuthRequest oauthRequest) {
        try {
            SocialType provider = oauthRequest.provider();
            log.debug("소셜 로그인 요청: {}", provider);
            
            SocialOAuth2Service socialOAuth2Service = socialServices.get(provider);
            if (socialOAuth2Service == null) {
                log.error("지원하지 않는 소셜 로그인: {}", provider);
                throw new BaseException(CommonErrorCode.INVALID_INPUT_REQUEST, "지원하지 않는 소셜 로그인입니다.");
            }
            log.debug("provider: {} -> socialOAuth2Service: {}", provider, socialOAuth2Service.getClass().getSimpleName());

            return socialOAuth2Service.getUserInfo(oauthRequest);
        }
        catch (OAuth2Exception e) {
            log.error("소셜 로그인 실패", e);
            switch (e.getErrorCode()) {
                case CLIENT -> throw new BaseException(CommonErrorCode.INVALID_INPUT_REQUEST, e);
                case PROVIDER -> throw new TemporaryException(5);
                case SERVER -> throw new BaseException(CommonErrorCode.INTERNAL_SERVER_ERROR, "소셜 로그인 서버 오류");
                default -> throw new BaseException(CommonErrorCode.INVALID_INPUT_REQUEST, "소셜 로그인 서버 오류");
            }
        }
    }

    private User saveOrUpdateUserAndGet(@NonNull OAuth2Data oauth2Data) {
        User user = findOrCreateUser(oauth2Data);
        userRepository.save(user);

        return user;
    }

    private User findOrCreateUser(OAuth2Data oauth2Data) {
        String username = oauth2Data.getProvider() + "_" + oauth2Data.getProviderId();

        return userRepository.findByUsername(username)
                .map(existingUser -> updateUserIfNeeded(existingUser, oauth2Data))
                .orElseGet(() -> createNewUser(username, oauth2Data));
    }

    private User updateUserIfNeeded(User user, OAuth2Data oauth2Data) {
        if (!Objects.equals(user.getNickName(), oauth2Data.getNickName())) {
            String nickName = oauth2Data.getNickName();
            NickName.tryCreate(nickName)
                    .ifPresent(n -> {
                        NickNameTag uniqueNickNameTag = nickNameTagGenerator.generateTag(n);
                        user.setName(uniqueNickNameTag.getNickName().get());
                        user.setNickNameTag(uniqueNickNameTag.getTag());
                    });
        }

        return user;
    }

    private User createNewUser(String username, OAuth2Data oauth2Data) {
        String nickName = oauth2Data.getNickName();
        NickName validNickName = NickName.tryCreate(nickName)
                .orElse(new NickName(generateRandomNickname()));

        NickNameTag uniqueNickNameTag = nickNameTagGenerator.generateTag(validNickName);

        return User.builder()
                .username(username)
                .password(null)
                .name(oauth2Data.getName())
                .nickName(uniqueNickNameTag.getNickName().get())
                .nickNameTag(uniqueNickNameTag.getTag())
                .email(oauth2Data.getEmail())
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

        return verb + "" + adjective + "" + animal;
    }

}
