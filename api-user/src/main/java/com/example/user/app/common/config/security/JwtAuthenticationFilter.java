package com.example.user.app.common.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.common.enums.CommonErrorCode;
import com.example.common.exception.BaseException;
import com.example.common.model.ApiResponse;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import com.example.common.util.ObjectMapperUtil;
import com.example.user.app.application.auth.domain.Device;
import com.example.user.app.application.auth.enums.AuthErrorCode;
import com.example.user.app.common.dto.security.SecurityUser;

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

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = resolveToken(request);                                       // 1. JWT 추출 + 디바이스 검증
            JwtUserDetails userDetails = extractUserDetailsFromToken(token);            // 2. 사용자 정보 추출
            Authentication authentication = buildAuthentication(userDetails);           // 3. 인증 객체 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (BaseException e) {
            setErrorResponse(response, e);
            return;
        }
        catch (Exception e) {
            logger.error("JWT 인증 필터에서 예외 발생", e);
            setErrorResponse(response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * JWT 추출 및 디바이스 정보 일치 검증
     */
    private String resolveToken(HttpServletRequest request) {
        String token = extractTokenFromHeader(request)
                .orElseThrow(() -> {
                    logger.warn("Authorization 헤더에서 JWT 추출 실패");
                    return new BaseException(AuthErrorCode.UNAUTHORIZED_BAD_CREDENTIALS_ACCESS_TOKEN_NOT_FOUND);
                });

        Device reqDevice = extractDeviceFromHeader(request)
                .orElseThrow(() -> {
                    logger.warn("디바이스 정보 추출 실패");
                    return new BaseException(AuthErrorCode.UNAUTHORIZED_DEVICE_NOT_FOUND);
                });

        if (JwtUtil.invalidToken(token) || CommonUtil.isEmpty(JwtUtil.getUserId(token))) {
            logger.warn("JWT 유효성 검사 실패");
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
        }

        Map<String, Object> claims = JwtUtil.getClaimsMap(token);
        Device tokenDevice = ObjectMapperUtil.getInstance().convertValue(claims.get("device"), Device.class);

        if (!reqDevice.equals(tokenDevice)) {
            logger.warn("디바이스 정보 불일치: 요청 디바이스={}, JWT 디바이스={}", reqDevice, tokenDevice);
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN_DEVICE_MISMATCH);
        }

        logger.debug("JWT 유효성 검사 통과");
        return token;
    }

    /**
     * Authorization 헤더에서 JWT 추출
     */
    private Optional<String> extractTokenFromHeader(HttpServletRequest request) {
        logger.debug("JWT 추출 시도");

        String header = request.getHeader(JwtUtil.AUTHORIZATION);
        if (CommonUtil.isEmpty(header) || !header.startsWith(JwtUtil.BEARER_PREFIX)) {
            logger.warn("Authorization 헤더가 없거나 Bearer 타입이 아님");
            return Optional.empty();
        }

        return Optional.of(header.substring(JwtUtil.BEARER_PREFIX.length()));
    }

    /**
     * 요청 헤더에서 디바이스 정보 추출
     */
    private Optional<Device> extractDeviceFromHeader(HttpServletRequest request) {
        logger.debug("Device 추출 시도");

        String deviceId = request.getHeader("Device-Id");
        String platform = request.getHeader("Platform");
        String browser = request.getHeader("Browser");

        if (CommonUtil.hasEmpty(deviceId, platform, browser)) {
            logger.warn("디바이스 헤더 정보 부족 deviceId={}, platform={}, browser={}", deviceId, platform, browser);
            return Optional.empty();
        }

        return Optional.of(new Device(deviceId, platform, browser));
    }

    /**
     * Jwt 에서 사용자 정보 추출
     */
    private JwtUserDetails extractUserDetailsFromToken(String token) {
        logger.debug("Jwt 에서 사용자 정보 추출 시도");

        UUID id = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        List<String> permission = JwtUtil.getUserPermission(token);

        if (CommonUtil.hasEmpty(id, username) || permission == null) {
            logger.warn("JWT에 필수 사용자 정보(id, username, permission)가 없음");
            throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
        }

        logger.debug("JWT 사용자 정보 추출 성공: id={}, username={}", id, username);
        return new JwtUserDetails(token, id, username, permission);
    }

    /**
     * 사용자 정보를 기반으로 Spring Security Authentication 생성
     */
    private Authentication buildAuthentication(JwtUserDetails jwtUserDetails) {
        logger.debug("Authentication 객체 생성 시도: username={}", jwtUserDetails.username());

        SecurityUser principal = new SecurityUser(
                jwtUserDetails.id(),
                jwtUserDetails.username(),
                "",
                jwtUserDetails.permission());

        List<GrantedAuthority> authorities = jwtUserDetails.permission().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(principal, jwtUserDetails.token(), authorities);
    }

    /**
     * 인증 실패 시 JSON 에러 응답 반환
     */
    private void setErrorResponse(HttpServletResponse response, Exception e) throws IOException {
        // 기본 응답 코드와 메시지 설정
        int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String errorMessage = CommonErrorCode.INTERNAL_SERVER_ERROR.getLogMessage();

        // 커스텀 예외일 경우, 실제 에러 코드와 메시지로 대체
        if (e instanceof BaseException baseEx) {
            statusCode = baseEx.getErrorCode().getCode();
            errorMessage = baseEx.getMessage();
        }

        // 응답 객체 세팅
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // JSON 응답 객체 생성
        ApiResponse<Void> apiResponse = new ApiResponse<>(statusCode, errorMessage, null, null);
        String json = ObjectMapperUtil.getInstance().writeValueAsString(apiResponse);

        // 응답 본문 출력
        try (PrintWriter writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }

    private record JwtUserDetails(
        String token,
        UUID id,
        String username,
        List<String> permission
    ) {}

}
