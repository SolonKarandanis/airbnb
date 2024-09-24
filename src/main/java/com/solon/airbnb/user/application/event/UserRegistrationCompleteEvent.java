package com.solon.airbnb.user.application.event;

import com.solon.airbnb.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserRegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public UserRegistrationCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
