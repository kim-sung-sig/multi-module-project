package com.example.common.exception;

import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(@NonNull Map<String, String> errors) {
        super("");
        this.errors = Collections.unmodifiableMap(errors);
    }

}
