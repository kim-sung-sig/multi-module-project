package com.example.user.app.infra.entity.exception;

import jakarta.persistence.EntityNotFoundException;

public class UserProfilePictureNotFoundException extends EntityNotFoundException {

    private static final String DEFAULT_MESSAGE = "USER_PROFILE_PICTURE_NOT_FOUND";

    public UserProfilePictureNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    // 커스텀 메시지를 받는 생성자도 추가
    public UserProfilePictureNotFoundException(String message) {
        super(message);
    }

    public UserProfilePictureNotFoundException(Exception cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public UserProfilePictureNotFoundException(String message, Exception cause) {
        super(message, cause);
    }

}
