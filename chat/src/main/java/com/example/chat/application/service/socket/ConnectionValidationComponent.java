package com.example.chat.application.service.socket;

import com.example.chat.application.service.validator.ChatRoomAccessValidator;
import com.example.chat.dto.socket.WebSocketConnectionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ConnectionValidationComponent {
    private final ChatRoomAccessValidator chatRoomValidator;

    public Mono<WebSocketConnectionContext> validate(WebSocketConnectionContext context) {
        return chatRoomValidator.valid(context.getChatRoomId(), context.getUserId())
                .thenReturn(context); // 검증 성공 시, 컨텍스트를 그대로 다음으로 전달
    }
}
