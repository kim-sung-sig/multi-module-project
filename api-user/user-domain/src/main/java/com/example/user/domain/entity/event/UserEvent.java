package com.example.user.domain.entity.event;

import org.springframework.context.ApplicationEvent;

import com.example.common.enums.EventType;
import com.example.user.domain.entity.User;

import lombok.Getter;

@Getter
public class UserEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private final User user;
    private final EventType eventType;

    public UserEvent(Object source, User user, EventType eventType) {
        super(source);
        this.user = user;
        this.eventType = eventType;
    }

}
