package com.example.user.app.common.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.common.exception.BaseException;
import com.example.common.exception.TemporaryException;
import com.example.common.exception.ValidationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String ERRORS = "errors";
    public static final String RETRY_AFTER = "retryAfterSeconds";

    // private final ErrorLogRepository errorLogRepository;

    // 기본 에러 포맷
    private Map<String, Object> createErrorResponse(String code, String message, Map<String, String> errors) {
        Map<String, Object> body = new HashMap<>();
        body.put(CODE, code);
        body.put(MESSAGE, message);

        if (errors != null) {
            body.put(ERRORS, errors);
        }

        return body;
    }

    private Map<String, Object> createErrorResponse(String code, String message) {
        return createErrorResponse(code, message, null);
    }

    private void logError(Exception e, HttpServletRequest request) {
        try {
            log.error("Exception occurred: {}", e.getMessage(), e);
            // ErrorLogEntity errorLog = ErrorLogEntity.of(e, request);
            // errorLogRepository.save(errorLog);
        } catch (Exception ex) {
            log.error("Error while logging exception: {}", ex.getMessage(), ex);
        }
    }

    // 일반적인 예외 처리 (시스템 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e, HttpServletRequest request) {
        Map<String, Object> body = createErrorResponse("INTERNAL_SERVER_ERROR", "시스템 오류가 발생했습니다.");
        logError(e, request);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Validation 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> body = createErrorResponse("VALIDATION_ERROR", "입력값이 올바르지 않습니다.", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Validation 예외
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(ValidationException e) {
        Map<String, Object> body = createErrorResponse("VALIDATION_ERROR", "입력값이 올바르지 않습니다.", e.getErrors());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 일시적인 예외 처리
    @ExceptionHandler(TemporaryException.class)
    public ResponseEntity<Map<String, Object>> handleTemporaryException(TemporaryException e) {
        Map<String, Object> body = createErrorResponse("TEMPORARY_ERROR", e.getMessage());
        body.put(RETRY_AFTER, e.getRetryAfterSeconds());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header(HttpHeaders.RETRY_AFTER, String.valueOf(e.getRetryAfterSeconds()))
                .body(body);
    }

    // BusinessException 예외
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BaseException e) {
        Map<String, Object> body = createErrorResponse("BUSINESS_ERROR", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
