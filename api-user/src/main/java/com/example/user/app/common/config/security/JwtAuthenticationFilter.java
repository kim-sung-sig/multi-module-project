package com.example.user.app.common.config.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.application.auth.exception.CustomAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.common.util.ObjectMapperUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.common.dto.security.SecurityUser;

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
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        Optional<String> tokenOpt = extractTokenFromHeader(request);
        if (tokenOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = tokenOpt.get();

            validateToken(token);
            Device reqDevice = validateDevice(request, token);
            JwtUserDetails userDetails = extractUserDetailsFromToken(token);
            
            Authentication authentication = buildAuthentication(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            logger.debug("JWT 인증 성공");
        }
        catch (CustomAuthenticationException e) {
            logger.debug("JWT 인증 실패: {}", e.getMessage());
            request.setAttribute("jwtException", e);
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * Authorization 헤더에서 JWT 추출
     */
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String bearer = OAuth2AccessToken.TokenType.BEARER.getValue() + " ";
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(bearer))
                .map(h -> h.substring(bearer.length()));
    }

    /**
     * 요청 헤더에서 디바이스 정보 추출
     */
    private Optional<Device> extractDeviceFromHeader(HttpServletRequest request) {
        String deviceId = request.getHeader("Device-Id");
        String platform = request.getHeader("Platform");
        String browser = request.getHeader("Browser");

        return Optional.ofNullable(deviceId)
                .filter(id -> !CommonUtil.hasEmpty(id, platform, browser))
                .map(id -> new Device(deviceId, platform, browser));
    }

    /**
     * Jwt 에서 사용자 정보 추출
     */
    private JwtUserDetails extractUserDetailsFromToken(String token) {
        UUID id = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        List<String> permission = JwtUtil.getUserPermission(token);

        if (CommonUtil.hasEmpty(id, username) || permission == null) {
            throw new CustomAuthenticationException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
        }

        return new JwtUserDetails(token, id, username, permission);
    }

    /**
     * Authentication 생성
     */
    private Authentication buildAuthentication(JwtUserDetails jwtUserDetails) {
        SecurityUser principal = new SecurityUser(
                jwtUserDetails.id(),
                jwtUserDetails.username(),
                "",
                jwtUserDetails.permission()
        );

        List<GrantedAuthority> authorities = jwtUserDetails.permission().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(principal, jwtUserDetails.token(), authorities);
    }
    
    private void validateToken(String token) {
        if (JwtUtil.invalidToken(token)) {
            throw new CustomAuthenticationException(AuthErrorCode.UNAUTHORIZED_EXPIRED_TOKEN);
        }
        if (CommonUtil.isEmpty(JwtUtil.getUserId(token))) {
            throw new CustomAuthenticationException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
        }
    }
    
    private Device validateDevice(HttpServletRequest request, String token) {
        Device reqDevice = extractDeviceFromHeader(request)
                .orElseThrow(() -> new CustomAuthenticationException(AuthErrorCode.UNAUTHORIZED_DEVICE_NOT_FOUND));
        
        Device tokenDevice = ObjectMapperUtil.getInstance()
                .convertValue(JwtUtil.getClaimsMap(token).get("device"), Device.class);
        
        if (!tokenDevice.equals(reqDevice)) {
            throw new CustomAuthenticationException(AuthErrorCode.UNAUTHORIZED_INVALID_DEVICE);
        }
        
        return reqDevice;
    }

    /**
     * JWT UserDetails 내부 record
     */
    private record JwtUserDetails(
            String token,
            UUID id,
            String username,
            List<String> permission
    ) {}

}