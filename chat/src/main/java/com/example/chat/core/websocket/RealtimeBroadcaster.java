package com.example.chat.core.websocket;

import com.example.chat.application.facade.MessageSentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealtimeBroadcaster {

//    private final WebSocketSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener
    public void handleMessageSentEvent(MessageSentEvent event) {
//        var message = event.getSavedMessage();
//        Long chatRoomId = message.getChatRoomId();
//
//        try {
//            String payload = objectMapper.writeValueAsString(message);
//            sessionManager.broadcastToRoom(chatRoomId, payload).subscribe();
//        } catch (Exception e) {
//            log.error("Failed to serialize message for broadcast", e);
//        }
    }
}
