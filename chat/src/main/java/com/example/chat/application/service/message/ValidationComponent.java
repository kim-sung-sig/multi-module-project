package com.example.chat.application.service.message;

import com.example.chat.application.service.validator.ChatRoomAccessValidator;
import com.example.chat.dto.message.MessageProcessingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ValidationComponent {
    private final ChatRoomAccessValidator chatRoomValidator;

    public Mono<MessageProcessingContext> validate(MessageProcessingContext context) {
        return chatRoomValidator.valid(context.getRequest().getChatRoomId(), context.getRequest().getSenderId())
                .thenReturn(context); // 검증 성공 시, 원본 context를 그대로 다음으로 넘김
    }
}
