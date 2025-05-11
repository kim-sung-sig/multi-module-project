package com.example.common.util;

import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        EventPublisher.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    public void init() {
        if (applicationEventPublisher == null) {
            throw new RuntimeException("applicationEventPublisher is null");
        }
    }

    public static void publish(Object event) {
        if (applicationEventPublisher == null) {
            return;
        }
        applicationEventPublisher.publishEvent(event);
    }

}
