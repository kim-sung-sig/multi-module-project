package com.example.chat.application.service.socket;

import com.example.chat.application.service.PresenceManager;
import com.example.chat.dto.socket.WebSocketConnectionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ConnectionSetupComponent {
    private final PresenceManager presenceManager;
//    private final WebSocketSessionManager sessionManager;
    private final ApplicationEventPublisher eventPublisher;

    public Mono<WebSocketConnectionContext> setup(WebSocketConnectionContext context) {
        // Redis와 Session Manager에 사용자 접속 정보 등록
        return presenceManager.userEntered(context.getChatRoomId(), context.getUserId())
//                .then(sessionManager.register(context.getUserId(), context.getSession()))
                .doOnSuccess(v -> {
                    // 성공 시, 비동기 로깅 및 알림을 위해 이벤트 발행
//                    UserEnteredRoomEvent event = new UserEnteredRoomEvent(this, context.getUserId(), context.getChatRoomId());
//                    eventPublisher.publishEvent(event);
                })
                .thenReturn(context);
    }
}
