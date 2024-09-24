package com.solon.airbnb.user.application.service;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.user.domain.VerificationToken;

public interface VerificationTokenService {

    public Boolean validateToken(VerificationToken verificationToken) throws BusinessException;

    public VerificationToken findByToken(String theToken);
}
