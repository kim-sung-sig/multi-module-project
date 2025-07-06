package com.example.chat.application.facade;

import com.example.chat.application.service.socket.ConnectionSetupComponent;
import com.example.chat.application.service.socket.ConnectionValidationComponent;
import com.example.chat.dto.socket.WebSocketConnectionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebSocketConnectionFacade {

    private final ConnectionValidationComponent validationComponent;
    private final ConnectionSetupComponent setupComponent;

    public Mono<Void> processConnection(WebSocketSession session, Long userId, Long chatRoomId) {
        // 1. 초기 컨텍스트 생성
        WebSocketConnectionContext initialContext = new WebSocketConnectionContext(session, userId, chatRoomId);

        // 2. 각 컴포넌트를 순차적으로 실행하는 리액티브 파이프라인 조립
        return Mono.just(initialContext)
                .flatMap(validationComponent::validate)
                .flatMap(setupComponent::setup)
                .then(); // 모든 연결 설정 작업이 끝나면 완료 신호 전송
    }
}
