package com.example.common.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private static SecretKey secretKey;
    private static JwtParser jwtParser;

    @Value("${jwt.secret-key}")
    private String originSecretKey;

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final long ACCESS_TOKEN_TTL = 5 * 60; // 5분
    public static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60; // 7일

    @PostConstruct
    public void init() {
        JwtUtil.secretKey = new SecretKeySpec(originSecretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        JwtUtil.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    /**
     * JWT 생성
     */
    public static String generateToken(Map<String, Object> claims, long second) {
        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(second)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Refresh Token 생성 (껍데기, UUID만 포함)
     */
    public static String generateRefreshToken(long second) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(UUID.randomUUID().toString() + System.currentTimeMillis()) // 랜덤 UUID + 시간
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(second)))
                .signWith(secretKey)
                .compact();
    }

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
        return UUID.fromString(getClaims(token).get("id", String.class));
    }

    public static String getUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public static List<String> getUserPermission(String token) {
        Object raw = getClaims(token).get("permission");
        if (raw instanceof List<?>) {
            return ((List<?>) raw).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static Date getExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    private static Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload();
    }

}