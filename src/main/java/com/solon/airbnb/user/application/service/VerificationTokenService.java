package com.solon.airbnb.user.application.service;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.domain.VerificationToken;

public interface VerificationTokenService {

    public Boolean validateToken(VerificationToken verificationToken) throws BusinessException;

    public VerificationToken findByToken(String theToken);

    public void saveUserVerificationToken(User theUser, String token);
}
