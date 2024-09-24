package com.solon.airbnb.email.application.service;

import com.solon.airbnb.email.application.dto.EmailDTO;
import com.solon.airbnb.email.domain.Email;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.email.repository.EmailAttachmentRepository;
import com.solon.airbnb.email.repository.EmailRepository;
import com.solon.airbnb.email.repository.EmailTypeRepository;
import com.solon.airbnb.shared.exception.AirbnbException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("emailService")
public class EmailServiceBean implements EmailService{

    private static final Logger log = LoggerFactory.getLogger(EmailServiceBean.class);

    private final EmailRepository emailRepository;
    private final EmailTypeRepository emailTypeRepository;
    private final EmailAttachmentRepository emailAttachmentRepository;
    private final EmailService emailService;

    public EmailServiceBean(
            EmailRepository emailRepository,
            EmailTypeRepository emailTypeRepository,
            EmailAttachmentRepository emailAttachmentRepository,
            EmailService emailService) {
        this.emailRepository = emailRepository;
        this.emailTypeRepository = emailTypeRepository;
        this.emailAttachmentRepository = emailAttachmentRepository;
        this.emailService = emailService;
    }

    @Override
    public void saveAndSendEmail(Email eMess) throws AirbnbException {

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
    public Integer saveEmail(Email email, String status) throws AirbnbException {
        return 0;
    }

    @Override
    public EmailDTO convertToDTO(Email email, Boolean withMsgBody) {
        return null;
    }

    @Override
    public Map<String, String> getEmailColumnMap() {
        return Map.of();
    }

    @Override
    public Map<String, String> getEmailSortingFieldsMap() {
        return Map.of();
    }
}
