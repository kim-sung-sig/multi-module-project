package com.example.chat.dto.socket;

import lombok.Getter;
import org.springframework.web.reactive.socket.WebSocketSession;

@Getter
public class WebSocketConnectionContext {

    private final WebSocketSession session;
    private final Long userId;
    private final Long chatRoomId;

    public WebSocketConnectionContext(WebSocketSession session, Long userId, Long chatRoomId) {
        this.session = session;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }
}
