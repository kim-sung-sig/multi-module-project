package com.example.user.app.infra.entity.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE = "USER_NOT_FOUND";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    // 커스텀 메시지를 받는 생성자도 추가
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Exception cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public UserNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
