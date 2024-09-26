package com.solon.airbnb.email.application.service;

import com.solon.airbnb.email.constants.EMailConstants;
import com.solon.airbnb.email.domain.Email;
import com.solon.airbnb.email.domain.EmailAttachment;
import com.solon.airbnb.email.domain.EmailStatus;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.RepException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EMailDelegateServiceBean extends GenericServiceBean implements EMailDelegateService{

    private static final Logger log = LoggerFactory.getLogger(EMailDelegateServiceBean.class);

    private final EmailService emailService;

    public EMailDelegateServiceBean(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendVerificationEmail(String url, User user)  throws AirbnbException{
        Map<String, Object> mailParams = new HashMap<>();
        mailParams.put(EMailConstants.EMAIL_ACTION, EMailConstants.REGISTRATION_USER_ACTION);
        mailParams.put("FIRST_NAME", user.getFirstName());

        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        sendMail(user.getEmail(), senderName,subject,mailContent,mailParams);
    }

    protected void sendMail(String to, String from, String subject,String mailContent, Map<String, Object> mailParams, String details1)
            throws AirbnbException {
        try {
            Email mail = initializeEmail(to,from,subject,mailContent,mailParams,details1);
            emailService.saveAndSendEmail(mail);
        }
        catch (RepException e) {
            throw new AirbnbException("error.sending.mail", e);
        }
    }

    protected void sendMail(String to, String from, String subject,String mailContent, Map<String, Object> mailParams) throws AirbnbException {
        sendMail(to, from,subject,mailContent, mailParams, null);
    }

    protected void sendMailWithAttachements(String to, String from, String subject,String mailContent, Map<String, Object> mailParams, List<EmailAttachment> attachments,
                                          String details1) throws AirbnbException{
        try {
            Email mail = initializeEmail(to,from,subject,mailContent,mailParams,details1);
            if(!CollectionUtils.isEmpty(attachments)){
                mail.setEmailAttachments(attachments);
            }
            emailService.saveAndSendEmail(mail);
        }
        catch (RepException e) {
            throw new AirbnbException("error.sending.mail", e);
        }

    }

    protected void sendMailWithAttachements(String to, String from, String subject,String mailContent, Map<String, Object> mailParams, List<EmailAttachment> attachments)
            throws AirbnbException{
        sendMailWithAttachements(to,from,subject,mailContent,mailParams,attachments);
    }

    protected Email initializeEmail(String to, String from, String subject,String mailContent, Map<String, Object> mailParams,String details1){
        Email mail = new Email();
        mail.setHeaderTo(to);
        mail.setHeaderFrom(from);
        mail.setHeaderSubject(subject);
        mail.setMessageBody(mailContent);
        EmailType emailType = emailService.getEmailTypeByKey((String) mailParams.get((EMailConstants.EMAIL_ACTION)));
        log.info("EMailDelegateServiceBean->initializeEmail->emailType: {}" , emailType.getResourceKey());
        mail.setEmailType(emailType);
        mail.setEmailTypesId(emailType.getId());
        mail.setStatus(EmailStatus.SENT);
        mail.setDetails1(details1);
        return mail;
    }

}
