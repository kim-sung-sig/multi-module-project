package com.example.chat.application.service;

import com.example.chat.application.facade.WebSocketConnectionFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final WebSocketConnectionFacade connectionFacade;
    private final PresenceManager presenceManager;
//    private final MessageService messageService; // 메시지 수신 처리를 위함

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        final Long chatRoomId = extractChatRoomIdFromSession(session);

        // 1. 세션에서 인증된 사용자 ID를 비동기적으로 가져옵니다.
        Mono<Long> userIdMono = session.getHandshakeInfo().getPrincipal()
                .map(principal -> Long.valueOf(principal.getName()))
                .switchIfEmpty(Mono.error(new SecurityException("User not authenticated.")));

        // 2. Facade를 통해 연결 설정 파이프라인을 실행합니다.
        return userIdMono.flatMap(userId ->
                connectionFacade.processConnection(session, userId, chatRoomId)
                        .then(
                                // 3. 연결이 성공적으로 수립된 후, 메시지 수신을 시작합니다.
                                session.receive()
                                        .flatMap(webSocketMessage -> {
                                            // 받은 메시지를 MessageService로 전달하는 로직
                                            return Mono.empty(); // TODO: 수신 메시지 처리 로직 구현
                                        })
                                        .then()
                        )
                        .doFinally(signalType ->
                                // 4. 연결이 어떤 이유로든 종료될 때 항상 퇴장 처리를 합니다.
                                presenceManager.userLeft(chatRoomId, userId).subscribe()
                        )
        );
    }

    private Long extractChatRoomIdFromSession(WebSocketSession session) {
        // ... 이전과 동일
        return Long.valueOf(Arrays.stream(session.getHandshakeInfo().getUri().getQuery().split("&"))
                .filter(s -> s.startsWith("roomId="))
                .findFirst()
                .map(s -> s.substring("roomId=".length()))
                .orElseThrow(() -> new IllegalArgumentException("Query parameter 'roomId' is missing.")));
    }
}
