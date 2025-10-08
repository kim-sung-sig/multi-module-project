package com.example.chat.domain.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomService {

	public boolean userHasAccess(Long userId, UUID roomId) {
		return false;
	}

	public void createRoom(Object request) {

	}

}
