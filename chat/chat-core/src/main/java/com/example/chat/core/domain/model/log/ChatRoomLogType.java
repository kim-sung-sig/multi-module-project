package com.example.chat.core.domain.model.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatRoomLogType {
    ENTER("입장"),
    LEAVE("퇴장"),
    ;

    private final String description;
}
