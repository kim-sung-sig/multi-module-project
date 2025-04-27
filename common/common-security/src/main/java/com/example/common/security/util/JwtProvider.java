package com.example.common.security.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.common.security.model.JwtUserInfo;
import com.example.common.security.model.SecurityUser;
import com.example.common.security.model.UserRole;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {

    private static SecretKey secretKey;
    private static JwtParser jwtParser;

    @Value("${jwt.secret-key}")
    private String originSecretKey;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    public void init() {
        log.debug("JwtUtil init, originSecretKey: {}", originSecretKey);
        JwtProvider.secretKey = new SecretKeySpec(originSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        JwtProvider.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    /**
     * JWT 생성
     */
    public static String generateToken(SecurityUser securityUser, long second) {
        Instant now = Instant.now();
        Date nowDate = Date.from(now);
        Date expirationDate = Date.from(now.plusSeconds(second));

        return Jwts.builder()
                .claim("id", securityUser.id().toString())
                .claim("username", securityUser.username())
                .claim("role", securityUser.role().name())
                .issuedAt(nowDate)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token 생성 (껍데기, UUID만 포함)
     */
    public static String generateRefreshToken(long second) {
        Instant now = Instant.now();
        Date nowDate = Date.from(now);
        Date expirationDate = Date.from(now.plusSeconds(second));

        return Jwts.builder()
                .subject(UUID.randomUUID().toString()) // 랜덤 UUID
                .issuedAt(nowDate)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    // public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
    //     return Optional.ofNullable(request.getHeader(AUTHORIZATION))
    //             .filter(bearerToken -> bearerToken.startsWith(BEARER_PREFIX))
    //             .map(bearerToken -> bearerToken.substring(7));
    // }

    /**
     * JWT 검증
     */
    public static boolean validateToken(String token) {
        try{
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * JWT에서 사용자 정보 추출
     */
    public static UUID getUserId(String token) {
        return UUID.fromString(jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .get("id", String.class));
    }

    public static String getUsername(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public static UserRole getUserRole(String token) {
        return UserRole.valueOf(
                jwtParser
                        .parseSignedClaims(token)
                        .getPayload()
                        .get("role", String.class));
    }

    public static Date getExpiration(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload().getExpiration();
    }

    public static JwtUserInfo getUserInfo(String token) {
        UUID id = getUserId(token);
        String username = getUsername(token);
        UserRole role = getUserRole(token);
        return new JwtUserInfo(id, username, role);
    }

}