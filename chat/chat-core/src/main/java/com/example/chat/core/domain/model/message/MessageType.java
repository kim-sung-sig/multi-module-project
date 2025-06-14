package com.example.chat.core.domain.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    SYSTEM("시스템"),
    TEXT("텍스트"),
    IMAGE("이미지"),
    SCHEDULE("일정"),
    FILE("파일"),
    VOICE("음성"),
    VIDEO("비디오"),
    LOCATION("위치"),
    CONTACT("연락처"),
    STICKER("스티커"),
    REACTION("반응"),
    LINK("링크"),
    DELIVERED("전달"),
    ;

    // 다양한 메시지 타입 추가
    private final String description;
}
