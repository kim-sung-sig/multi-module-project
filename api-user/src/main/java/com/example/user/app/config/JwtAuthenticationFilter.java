package com.example.user.app.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.common.model.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 요청으로부터 JWT를 추출하고, 인증 정보를 SecurityContext에 설정하는 필터.
 * <p>
 * 요청의 Authorization 헤더에 포함된 JWT를 파싱하여 사용자 정보를 추출하고,
 * Spring Security의 인증 객체(Authentication)를 생성하여 SecurityContext에 등록한다.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * JWT 인증 필터의 핵심 로직을 수행한다.
     * <ol>
     *     <li>Authorization 헤더에서 JWT 추출</li>
     *     <li>JWT에서 사용자 정보 파싱</li>
     *     <li>사용자 정보를 기반으로 Authentication 생성</li>
     *     <li>SecurityContext에 인증 정보 등록</li>
     * </ol>
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> token = resolveToken(request);
        Optional<JwtUserDetails> extractUserDetailsFromToken = token.flatMap(this::extractUserDetailsFromToken);
        Optional<Authentication> authentication = extractUserDetailsFromToken.flatMap(this::buildAuthentication);
        authentication.ifPresent(this::setAuthentication);

        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 JWT를 추출하고 유효성을 검사한다.
     *
     * @param request HttpServletRequest
     * @return 유효한 JWT 문자열 (없을 경우 Optional.empty)
     */
    private Optional<String> resolveToken(HttpServletRequest request) {
        logger.debug("Authorization 헤더에서 JWT 추출 시도");

        String header = request.getHeader(JwtUtil.AUTHORIZATION);
        if (CommonUtil.isEmpty(header) || !header.startsWith(JwtUtil.BEARER_PREFIX)) {
            logger.warn("Authorization 헤더가 없거나 Bearer 타입이 아님");
            return Optional.empty();
        }

        String token = header.substring(JwtUtil.BEARER_PREFIX.length());

        if (!JwtUtil.validateToken(token)) {
            logger.warn("JWT 유효성 검사 실패");
            return Optional.empty();
        }

        logger.debug("JWT 유효성 검사 통과");
        return Optional.of(token);
    }

    /**
     * JWT에서 사용자 정보를 추출한다.
     *
     * @param token JWT 문자열
     * @return 추출된 사용자 정보 객체 (없을 경우 Optional.empty)
     */
    private Optional<JwtUserDetails> extractUserDetailsFromToken(String token) {
        try {
            logger.debug("JWT에서 사용자 정보 추출 시도");

            UUID id = JwtUtil.getUserId(token);
            String username = JwtUtil.getUsername(token);
            List<String> permission = JwtUtil.getUserPermission(token);

            if (CommonUtil.hasEmpty(id, username, permission)) {
                logger.warn("JWT에 필수 사용자 정보(id, username, permission)가 없음");
                return Optional.empty();
            }

            logger.debug("JWT 사용자 정보 추출 성공: id={}, username={}", id, username);
            return Optional.of(new JwtUserDetails(token, id, username, permission));
        } catch (Exception e) {
            logger.error("JWT에서 사용자 정보 추출 실패", e);
            return Optional.empty();
        }
    }

    /**
     * 사용자 정보를 기반으로 Spring Security의 Authentication 객체를 생성한다.
     *
     * @param jwtUserDetails JWT에서 추출한 사용자 정보
     * @return 인증 객체 (Authentication) (없을 경우 Optional.empty)
     */
    private Optional<Authentication> buildAuthentication(JwtUserDetails jwtUserDetails) {
        try {
            logger.debug("Authentication 객체 생성 시도: username={}", jwtUserDetails.username());

            SecurityUser principal = new SecurityUser(
                    jwtUserDetails.id(),
                    jwtUserDetails.username(),
                    "",
                    jwtUserDetails.permission());

            List<GrantedAuthority> authorities = jwtUserDetails.permission().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            logger.info("Authentication 객체 생성 완료 - SecurityContext에 등록 예정");
            return Optional.of(new UsernamePasswordAuthenticationToken(principal, jwtUserDetails.token(), authorities));

        } catch (Exception e) {
            logger.error("Authentication 객체 생성 실패", e);
            return Optional.empty();
        }
    }

    /**
     * 생성된 인증 정보를 SecurityContext에 설정한다.
     *
     * @param authentication 생성된 인증 객체
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private record JwtUserDetails(
        String token,
        UUID id,
        String username,
        List<String> permission
    ) {}

}
