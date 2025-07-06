package com.example.chat.application.service.validator;

public interface ChatRoomAccessValidator {

    boolean valid(Long chatRoomId, Long userId);

}
