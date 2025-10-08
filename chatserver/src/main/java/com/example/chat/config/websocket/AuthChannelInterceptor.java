package com.example.chat.config.websocket;

import com.example.chat.domain.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

	private final RoomService roomService;

	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		Objects.requireNonNull(accessor);

		// 1. 구독 시점 (SUBSCRIBE): 채팅방 접근 권한 인가
		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String destination = accessor.getDestination();
			Principal auth = accessor.getUser();

			if (auth == null || destination == null) {
				throw new SecurityException("Subscription failed: Not authenticated");
			}

			if (!canSubscribe(auth, destination)) {
				throw new AccessDeniedException("Not authorized to subscribe");
			}
		}

		return message;
	}

	private boolean canSubscribe(Principal auth, String destination) {
		// destination 예시: /topic/chat/room/{roomId} // TODO 상수로 관리
		if (!destination.startsWith("/topic/chat/room/")) return false;

		String roomIdStr = destination.substring("/topic/chat/room/".length());
		try {
			UUID roomId = UUID.fromString(roomIdStr);
			Long userId = Long.valueOf(auth.getName());

			return roomService.userHasAccess(userId, roomId);
		}
		catch (Exception e) {
			log.error("Invalid roomId in destination: {}", destination, e);
			return false;
		}
	}

}