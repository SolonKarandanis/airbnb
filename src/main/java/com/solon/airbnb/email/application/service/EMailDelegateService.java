package com.solon.airbnb.email.application.service;

import com.solon.airbnb.user.domain.User;

public interface EMailDelegateService {

    public void sendVerificationEmail(String url, User user);
}
