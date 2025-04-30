package com.example.user.domain.entity.event;

import org.springframework.context.ApplicationEvent;

import com.example.common.enums.EventType;
import com.example.user.domain.entity.UserProfilePicture;

import lombok.Getter;

@Getter
public class UserProfilePictureEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    private final UserProfilePicture profile;
    private final EventType eventType;

    public UserProfilePictureEvent(Object source, UserProfilePicture profile, EventType eventType) {
        super(source);
        this.profile = profile;
        this.eventType = eventType;
    }

}
