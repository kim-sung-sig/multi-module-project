package com.example.chat.app.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@RestControllerAdvice
public class TestApi {

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('QQ')")
    public Mono<ResponseEntity<String>> test(
            @Valid @ModelAttribute TestClass testClass,
            @AuthenticationPrincipal Jwt jwt
    ) {
        System.out.println(jwt);
        return Mono.just(ResponseEntity.ok("test"));
    }

    public record TestClass(
            @NotNull(message = "not id") Long id
    ){}

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<String>> error(WebExchangeBindException  ex) {
        String errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Mono.just(ResponseEntity.badRequest().body(errorMessages));
    }
}
