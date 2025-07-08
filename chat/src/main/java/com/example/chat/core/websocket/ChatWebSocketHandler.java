package com.example.chat.core.websocket;

import com.example.chat.application.facade.WebSocketConnectionFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final WebSocketConnectionFacade connectionFacade;
//    private final ConnectionManager connectionManager;
//    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public @NonNull Mono<Void> handle(@NonNull WebSocketSession session) {
        final Long chatRoomId = extractChatRoomIdFromSession(session);

        // 아 webflux chating 너무 어렵다..
        Mono<Long> userIdMono = session.getHandshakeInfo().getPrincipal()
                .map(principal -> Long.valueOf(principal.getName()))
                .switchIfEmpty(Mono.error(new SecurityException("User not authenticated.")));

        return userIdMono.flatMap(userId ->
                // 1. Facade를 통해 연결 설정 파이프라인 실행
                connectionFacade.processConnection(session, userId, chatRoomId)
                        .then(
                                // 2. 연결 성공 후, 메시지 수신 스트림 처리
                                session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
//                                        .flatMap(jsonPayload -> messageService.processIncomingMessage(jsonPayload, userId, chatRoomId))
                                        .then()
                        )
//                        .doFinally(signalType ->
//                                // 3. 연결 종료 시 항상 퇴장 및 세션 정리
//                                connectionManager.cleanupConnection(chatRoomId, userId).subscribe()
//                        )
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
