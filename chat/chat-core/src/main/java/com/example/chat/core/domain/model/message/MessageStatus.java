package com.example.chat.core.domain.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {
    SENT("전송"),
    DELIVERED("전달"),
    READ("읽음"),
    DELETED("삭제"),
    FAILED("실패"),
    ;

    private final String description;
}
