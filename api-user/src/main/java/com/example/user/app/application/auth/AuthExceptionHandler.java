package com.example.user.app.application.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.user.app.api.GlobalExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = {})
public class AuthExceptionHandler {

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<Map<String, Object>> handleLockedException(CustomAuthException e) {
        Map<String, Object> errorResponse = Map.of(
                GlobalExceptionHandler.CODE, e.getCode().name(),
                GlobalExceptionHandler.MESSAGE, e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

}
