package com.example.chat.core.chatcore.domain.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    TEXT("텍스트"),
    IMAGE("이미지"),
    SCHEDULE("일정"),
    FILE("파일"),
    ;

    // 다양한 메시지 타입 추가
    private final String description;
}
