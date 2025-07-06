package com.example.chat.application.facade;

import com.example.chat.dto.message.MessageProcessingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersistenceComponent {
    private final ChatMessageProcessor messageProcessor;

    public Mono<MessageProcessingContext> processAndSave(MessageProcessingContext context) {
        return messageProcessor.process(context.getRequest())
                .map(savedMessage -> {
                    context.setSavedMessage(savedMessage); // 저장된 결과를 context에 담고
                    return context; // 업데이트된 context를 다음으로 넘김
                });
    }
}
