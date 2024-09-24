package com.solon.airbnb.email.application.service;

import com.solon.airbnb.email.application.dto.EmailDTO;
import com.solon.airbnb.email.domain.Email;
import com.solon.airbnb.email.domain.EmailAttachment;
import com.solon.airbnb.email.domain.EmailStatus;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.email.repository.EmailAttachmentRepository;
import com.solon.airbnb.email.repository.EmailRepository;
import com.solon.airbnb.email.repository.EmailTypeRepository;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("emailService")
public class EmailServiceBean extends GenericServiceBean implements EmailService{

    private static final Logger log = LoggerFactory.getLogger(EmailServiceBean.class);

    private final EmailRepository emailRepository;
    private final EmailTypeRepository emailTypeRepository;
    private final EmailAttachmentRepository emailAttachmentRepository;
    private final JavaMailSender mailSender;

    public EmailServiceBean(
            EmailRepository emailRepository,
            EmailTypeRepository emailTypeRepository,
            EmailAttachmentRepository emailAttachmentRepository,
            JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.emailTypeRepository = emailTypeRepository;
        this.emailAttachmentRepository = emailAttachmentRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void saveAndSendEmail(Email eMess) throws AirbnbException {
        Integer emailId = saveEmail(eMess,EmailStatus.PENDING);
//        try {
//            SerializableObjectMessageCreator eMsgCreator = new SerializableObjectMessageCreator(emailId);
//            jmsTemplate.send(mailsDestination, eMsgCreator);
//        } catch (Exception e) {
//            throw new EDException("errors.send.email", e);
//        }
    }

    @Override
    public void saveAndSendEmailSynchronous(Email eMess) throws AirbnbException {

    }

    @Override
    public List<EmailType> getEmailTypes() {
        return List.of();
    }

    @Override
    public Email getEmailById(Integer id) throws AirbnbException {
        return null;
    }

    @Override
    public void updateEmailStatus(Integer id, String status) {

    }

    @Override
    public void resendEmails(List<Integer> emailIds) throws AirbnbException {

    }

    @Override
    public EmailType getEmailTypeByKey(String resourceKey) {
        return null;
    }

    @Override
    public Integer saveEmail(Email email, EmailStatus status) throws AirbnbException {
        email.setStatus(status);
        email.setDateCreated(new Date());
        if (EmailStatus.SENT.equals(email.getStatus())) {
            email.setDateSent(new Date());
        }
        if(!CollectionUtils.isEmpty(email.getEmailAttachments())){
            for(EmailAttachment att : email.getEmailAttachments()){

            }
        }
        email = emailRepository.save(email);
        return email.getId();
    }

    @Override
    public EmailDTO convertToDTO(Email email, Boolean withMsgBody) {
        if (email == null) {
            return null;
        }
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setId(email.getId());
        emailDTO.setDateCreated(ConvertUtils.convert(email.getDateCreated()));
        emailDTO.setDateSent(ConvertUtils.convert(email.getDateSent()));
        emailDTO.setDetails1(email.getDetails1());
        emailDTO.setHeaderFrom(email.getHeaderFrom());
        emailDTO.setHeaderTo(email.getHeaderTo());
        emailDTO.setHeaderCc(email.getHeaderCc());
        emailDTO.setHeaderBcc(email.getHeaderBcc());
        emailDTO.setHeaderReplyTo(email.getHeaderReplyTo());
        emailDTO.setHeaderSubject(email.getHeaderSubject());
        emailDTO.setStatus(email.getStatus().getValue());
        emailDTO.setEmailTypeId(email.getEmailType().getId());
        emailDTO.setEmailTypeKey(email.getEmailType().getResourceKey());
        if (withMsgBody) {
            emailDTO.setMessageBody(email.getMessageBody());
        }
        return emailDTO;
    }

    @Override
    public Map<String, String> getEmailColumnMap() {
        return Map.of();
    }

    @Override
    public Map<String, String> getEmailSortingFieldsMap() {
        return Map.of();
    }

    protected InternetAddress[] parseAddress(String str) throws AddressException {
        InternetAddress[] retVal;
        if (str == null || "".equals(str)) {
            retVal = null;
        } else {
            retVal = InternetAddress.parse(str);
        }
        return retVal;
    }

    /**
     * This method generates a String useful for debug statements to log the
     * actuall recipients that emails were sent We prefer this method instead of
     * InternetAddress.toString(InternetAddress[]) to more "accurate" logging
     *
     * @param addresses
     * @return A string in comma (,) delimeted format. Last comma is not
     *         removed.
     */
    protected String dumpAddresses(InternetAddress[] addresses) {
        StringBuilder retVal = new StringBuilder(2048);
        if (addresses != null) {
            for (int i = 0; i < addresses.length; i++) {
                retVal.append(addresses[i]);
                retVal.append(',');
            }

        }
        return retVal.toString();
    }
}
