package com.solon.airbnb.email.application.service;

import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EMailDelegateServiceBean extends GenericServiceBean implements EMailDelegateService{
    @Override
    public void sendVerificationEmail(String url, User user) {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
    }
}
