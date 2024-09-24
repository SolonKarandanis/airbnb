package com.solon.airbnb.user.application.event;


import com.solon.airbnb.email.application.service.EMailDelegateService;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.user.application.service.VerificationTokenService;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserRegistrationCompleteEventListener implements ApplicationListener<UserRegistrationCompleteEvent> {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationCompleteEventListener.class);

    private final VerificationTokenService tokenService;
    private final EMailDelegateService eMailDelegateService;
    private User user;

    public UserRegistrationCompleteEventListener(
            VerificationTokenService tokenService,
            EMailDelegateService eMailDelegateService) {
        this.tokenService = tokenService;
        this.eMailDelegateService = eMailDelegateService;
    }

    @Override
    public void onApplicationEvent(UserRegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        user = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. Save the verification token for the user
        tokenService.saveUserVerificationToken(user, verificationToken);
        //4 Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        try {
            eMailDelegateService.sendVerificationEmail(url,user);
        } catch (AirbnbException e) {
            throw new RuntimeException(e);
        }
        log.info("UserRegistrationCompleteEventListener -> onApplicationEvent ->  url:  {}", url);
    }
}
