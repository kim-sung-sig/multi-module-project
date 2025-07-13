package com.example.chat.app.common.config;

import com.example.chat.app.common.dto.security.SecurityUser;
import com.example.common.util.CommonUtil;
import com.example.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

	//private final ChatRoomAccessValidator chatRoomAccessValidator;

	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		Objects.requireNonNull(accessor);

		// 1. 연결 시점 (CONNECT): JWT 인증
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String jwt = accessor.getFirstNativeHeader(JwtUtil.AUTHORIZATION);

			if (jwt != null && jwt.startsWith(JwtUtil.BEARER_PREFIX)) {
				String token = jwt.substring(JwtUtil.BEARER_PREFIX.length());
				if (JwtUtil.invalidToken(token)) {
					JwtUserDetails userDetails = extractUserDetailsFromToken(token);
					Authentication auth = buildAuthentication(userDetails);
					accessor.setUser(auth); // 세션에 인증 정보 저장
					log.info("STOMP Connect - User authenticated: {}", auth.getName());
					return message;
				}
			}
			log.warn("STOMP Connect - Authentication failed");
			// 인증 실패 시 예외를 던져 연결을 거부할 수 있습니다.
			throw new SecurityException("Authentication failed");
		}

		// 2. 구독 시점 (SUBSCRIBE): 채팅방 접근 권한 인가
		if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			String destination = accessor.getDestination();
			Authentication auth = (Authentication) accessor.getUser();

			if (auth == null || destination == null) {
				throw new SecurityException("Subscription failed: Not authenticated");
			}

			JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();

			UUID userId = userDetails.id();
			Long roomId = extractRoomIdFromDestination(destination);

			// 사용자가 해당 채팅방의 멤버인지 확인합니다. (블로킹 호출 - 개선 필요)
			// 실제 리액티브 환경에서는 이 부분을 비동기로 처리해야 합니다.
			//if (!chatRoomService.isMember(roomId, userId)) {
			//	log.warn("Subscription denied for user {} to room {}", userId, roomId);
			//	return null; // 메시지를 소멸시켜 구독을 막습니다.
			//}
			//log.info("User {} subscribed to room {}", userId, roomId);
		}

		return message;
	}

	private JwtUserDetails extractUserDetailsFromToken(String token) {
		log.debug("Jwt 에서 사용자 정보 추출 시도");

		UUID id = JwtUtil.getUserId(token);
		String username = JwtUtil.getUsername(token);
		List<String> permission = JwtUtil.getUserPermission(token);

		if (CommonUtil.hasEmpty(id, username) || permission == null) {
			log.warn("JWT에 필수 사용자 정보(id, username, permission)가 없음");
			throw new RuntimeException();
			//throw new BaseException(AuthErrorCode.UNAUTHORIZED_INVALID_TOKEN);
		}

		log.debug("JWT 사용자 정보 추출 성공: id={}, username={}", id, username);
		return new JwtUserDetails(token, id, username, permission);
	}

	private Authentication buildAuthentication(JwtUserDetails jwtUserDetails) {
		log.debug("Authentication 객체 생성 시도: username={}", jwtUserDetails.username());

		SecurityUser principal = new SecurityUser(
				jwtUserDetails.id(),
				jwtUserDetails.username(),
				"",
				jwtUserDetails.permission());

		List<GrantedAuthority> authorities = jwtUserDetails.permission().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		return new UsernamePasswordAuthenticationToken(principal, jwtUserDetails.token(), authorities);
	}

	private Long extractRoomIdFromDestination(String destination) {
		return 1L;
	}

	private record JwtUserDetails(
			String token,
			UUID id,
			String username,
			List<String> permission
	) {}
}
