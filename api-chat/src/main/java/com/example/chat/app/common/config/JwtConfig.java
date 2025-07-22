package com.example.chat.app.common.config;

import com.example.chat.app.common.dto.security.CustomJwtAuthenticationToken;
import com.nimbusds.jose.JWSAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key:YIPWu4GDG60vjc8ddrKsK4bmaSKK0pxE}")
    private String secretKey;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), JWSAlgorithm.HS256.getName());
        return NimbusReactiveJwtDecoder.withSecretKey(secretKeySpec).build();
    }

    //@Bean
    //public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
    //    ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
    //
    //    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
    //        List<String> roles = jwt.getClaimAsStringList("roles");
    //        List<String> authorities = jwt.getClaimAsStringList("authorities");
    //
    //        if (roles == null) roles = List.of();
    //        if (authorities == null) authorities = List.of();
    //
    //        List<GrantedAuthority> combinedAuthorities = Stream.concat(
    //                roles.stream().map(role -> "ROLE_" + role.toUpperCase()),
    //                authorities.stream()
    //        )
    //        .map(SimpleGrantedAuthority::new)
    //        .collect(Collectors.toUnmodifiableList());
    //
    //        return Flux.fromIterable(combinedAuthorities);
    //    });
    //
    //    return converter;
    //}

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveJwtDecoder jwtDecoder) {
        return authentication -> {
            String token = authentication.getCredentials().toString();
            return jwtDecoder.decode(token)
                    .map(jwt -> {
                        List<String> roles = Optional.ofNullable(jwt.getClaimAsStringList("roles"))
                                .orElse(List.of());
                        List<String> authorities = Optional.ofNullable(jwt.getClaimAsStringList("authorities"))
                                .orElse(List.of());

                        List<GrantedAuthority> grantedAuthorities = Stream.concat(
                                roles.stream().map(role -> "ROLE_" + role.toUpperCase()),
                                authorities.stream()
                        )
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableList());

                        return new CustomJwtAuthenticationToken(jwt, grantedAuthorities);
                    });
        };
    }
}
