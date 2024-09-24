package com.solon.airbnb.user.application.event;


import com.solon.airbnb.user.application.service.VerificationTokenService;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserRegistrationCompleteEventListener implements ApplicationListener<UserRegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationCompleteEvent.class);

    private final VerificationTokenService tokenService;
    private User theUser;

    public UserRegistrationCompleteEventListener(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onApplicationEvent(UserRegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        tokenService.saveUserVerificationToken(theUser, verificationToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;

        log.info("UserRegistrationCompleteEventListener -> onApplicationEvent ->  url:  {}", url);
    }
}
