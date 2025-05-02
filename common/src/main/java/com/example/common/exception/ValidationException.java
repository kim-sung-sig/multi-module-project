package com.example.common.exception;

import java.util.Collections;
import java.util.Map;

import org.springframework.lang.NonNull;

public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(@NonNull Map<String, String> errors) {
        super("");
        this.errors = Collections.unmodifiableMap(errors);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
