package com.example.chat.application.service.validator;

import com.example.chat.infra.persistence.room.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatRoomAccessValidator {

    private final ChatRoomRepository chatRoomRepository;

    public Mono<Void> valid(Long chatRoomId, Long userId) {
        return chatRoomRepository.isMember(chatRoomId, userId)
                .flatMap(isMember -> {
                    if (!isMember) {
                        return Mono.error(new RuntimeException()); // TODO
                    }
                    return Mono.empty();
                }).then();
    }

}
