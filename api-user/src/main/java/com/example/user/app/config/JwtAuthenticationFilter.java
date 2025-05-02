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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> token = resolveToken(request);
        Optional<Authentication> authentication = token.flatMap(this::buildAuthentication);
        authentication.ifPresent(this::setAuthentication);

        filterChain.doFilter(request, response);
    }

    /**
     * Request Header 에서 JWT 추출
     */
    private Optional<String> resolveToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(JwtUtil.AUTHORIZATION))
                .filter(token -> token.startsWith(JwtUtil.BEARER_PREFIX))
                .map(token -> token.substring(JwtUtil.BEARER_PREFIX.length()))
                .filter(JwtUtil::validateToken);
    }

    /**
     * 유효한 JWT에서 Authentication 생성
     */
    private Optional<Authentication> buildAuthentication(String token) {
        try {
            UUID id = JwtUtil.getUserId(token);
            String username = JwtUtil.getUsername(token);
            String password = "";  // 보통 "" 처리, 비밀번호는 사용하지 않음
            List<String> permission = JwtUtil.getUserPermission(token);

            // 빈 값 있으면 인증 안함
            if (CommonUtil.hasEmpty(id, username, permission)) {
                return Optional.empty();
            }

            SecurityUser principal = new SecurityUser(id, username, password, permission);

            List<GrantedAuthority> authorities = permission.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return Optional.of(new UsernamePasswordAuthenticationToken(principal, token, authorities));

        } catch (Exception e) {
            logger.error("JwtAuthenticationFilter 에러 발생", e);
            // 로그만 남기고 인증은 하지 않음
            return Optional.empty();
        }
    }

    /**
     * 시큐리티 인증객체 등록
     * @param authentication
     */
    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
