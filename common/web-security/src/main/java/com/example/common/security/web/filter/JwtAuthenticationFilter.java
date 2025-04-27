package com.example.common.security.web.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.common.security.model.JwtUserInfo;
import com.example.common.security.model.SecurityUser;
import com.example.common.security.util.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        Optional<String> token = this.getJwtFromRequest(request);

        if (token.isPresent() && JwtProvider.validateToken(token.get())) {
            JwtUserInfo userInfo = JwtProvider.getUserInfo(token.get());
            SecurityUser securityUser = new SecurityUser(userInfo);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                securityUser,                   // principal
                token.get(),                    // credentials
                securityUser.authorities()      // authorities
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(JwtProvider.AUTHORIZATION))
                .filter(bearerToken -> bearerToken.startsWith(JwtProvider.BEARER_PREFIX))
                .map(bearerToken -> bearerToken.substring(7));
    }

}
