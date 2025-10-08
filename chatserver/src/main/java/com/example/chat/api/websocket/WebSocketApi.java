package com.example.chat.api.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketApi {

	@MessageMapping("/room/{roomId}/send")
	public void sendMessage(
			@DestinationVariable String roomId,
			@Payload String message,
			@AuthenticationPrincipal Principal auth
	) {

	}

}