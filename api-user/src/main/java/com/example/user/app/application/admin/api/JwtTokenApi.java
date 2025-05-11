package com.example.user.app.application.admin.api;

import com.example.common.model.ApiResponse;
import com.example.user.app.application.admin.service.JwtTokenService;
import com.example.user.app.application.auth.dto.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
public class JwtTokenApi {

    private final JwtTokenService jwtTokenService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse< JwtTokenResponse >> getTokenList() {
        return null;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        return entity;
    }
}
