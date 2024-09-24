package com.solon.airbnb.user.application.service;

import com.solon.airbnb.shared.exception.BusinessException;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.user.domain.VerificationToken;
import com.solon.airbnb.user.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
@Transactional(readOnly = true)
public class VerificationTokenServiceBean implements VerificationTokenService{

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceBean(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional
    @Override
    public Boolean validateToken(VerificationToken verificationToken) throws BusinessException {
        if(verificationToken == null){
            throw new BusinessException("error.invalid.token");
        }
        if(verificationToken.getUser().getVerified()){
            throw new BusinessException("error.user.already.verified");
        }

        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(verificationToken);
            throw new BusinessException("error.expired.token");
        }
        return true;
    }

    @Override
    public VerificationToken findByToken(String theToken) {
        return verificationTokenRepository.findByToken(theToken);
    }

    @Transactional
    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        verificationTokenRepository.save(verificationToken);
    }
}