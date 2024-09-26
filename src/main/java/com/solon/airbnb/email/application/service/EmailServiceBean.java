package com.solon.airbnb.email.application.service;

import com.solon.airbnb.email.application.dto.EmailSearchRequestDTO;
import com.solon.airbnb.email.constants.EMailConstants;
import com.solon.airbnb.email.application.dto.EmailDTO;
import com.solon.airbnb.email.domain.Email;
import com.solon.airbnb.email.domain.EmailAttachment;
import com.solon.airbnb.email.domain.EmailStatus;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.email.repository.EmailAttachmentRepository;
import com.solon.airbnb.email.repository.EmailRepository;
import com.solon.airbnb.email.repository.EmailTypeRepository;
import com.solon.airbnb.fileinfo.application.service.FileService;
import com.solon.airbnb.fileinfo.constants.FileConstants;
import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.fileinfo.util.FileUtil;
import com.solon.airbnb.shared.common.mail.AttachmentDataSource;
import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Service("emailService")
public class EmailServiceBean extends GenericServiceBean implements EmailService{

    private static final Logger log = LoggerFactory.getLogger(EmailServiceBean.class);

    private final EmailRepository emailRepository;
    private final EmailTypeRepository emailTypeRepository;
    private final EmailAttachmentRepository emailAttachmentRepository;
    private final JavaMailSender mailSender;
    private final FileService fileService;

    public EmailServiceBean(
            EmailRepository emailRepository,
            EmailTypeRepository emailTypeRepository,
            EmailAttachmentRepository emailAttachmentRepository,
            JavaMailSender mailSender,
            FileService fileService) {
        this.emailRepository = emailRepository;
        this.emailTypeRepository = emailTypeRepository;
        this.emailAttachmentRepository = emailAttachmentRepository;
        this.mailSender = mailSender;
        this.fileService = fileService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAndSendEmail(Email eMess) throws AirbnbException {
        eMess = saveEmail(eMess,EmailStatus.PENDING);
        try{
            MimeMessage message = toMimeMessage(eMess);
            mailSender.send(message);
        }
        catch (MessagingException | UnsupportedEncodingException ex){
            throw new AirbnbException("errors.send.email", ex);
        }
//        try {
//            SerializableObjectMessageCreator eMsgCreator = new SerializableObjectMessageCreator(emailId);
//            jmsTemplate.send(mailsDestination, eMsgCreator);
//        } catch (Exception e) {
//            throw new EDException("errors.send.email", e);
//        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveAndSendEmailSynchronous(Email eMess) throws AirbnbException {
        try {
            Properties props = new Properties();
            props.setProperty(EMailConstants.EMAIL_SERVER_HOST_NAME, EMailConstants.EMAIL_SERVER_HOST_NAME);
            props.setProperty(EMailConstants.EMAIL_TRANSPORT_PROTOCOL_NAME, EMailConstants.EMAIL_TRANSPORT_PROTOCOL_VALUE);
            props.setProperty(EMailConstants.EMAIL_SERVICE_AUTHENTICATION, EMailConstants.EMAIL_SERVICE_AUTHENTICATION_VALUE);
            Session session = Session.getDefaultInstance(props, null);
            if (session == null) {
                log.debug("session is null!!!!!!");
                throw new AirbnbException("Could not obtain mail session");
            }
            // Construct the message
            MimeMessage msg = new MimeMessage(session);
            // Set "from" address
            InternetAddress fromAddress = new InternetAddress(eMess.getHeaderFrom());
            msg.setFrom(fromAddress);
            // Add "to" emails
            InternetAddress[] addresses = parseAddress(eMess.getHeaderTo());
            log.debug("sendEmailSynchronous: TO= {}" , dumpAddresses(addresses));
            msg.addRecipients(Message.RecipientType.TO,addresses);

            addresses = parseAddress(eMess.getHeaderCc());
            log.debug("sendEmailSynchronous: CC= {}" , dumpAddresses(addresses));
            msg.addRecipients(Message.RecipientType.CC, addresses);

            addresses = parseAddress(eMess.getHeaderBcc());
            log.debug("sendEmailSynchronous: BCC= {}" , dumpAddresses(addresses));
            msg.addRecipients(Message.RecipientType.BCC, addresses);

            addresses = parseAddress(eMess.getHeaderReplyTo());
            log.debug("sendEmailSynchronous: ReplyTo= {}" , dumpAddresses(addresses));
            msg.setReplyTo(addresses);

            // Set email subject
            if (eMess.getHeaderSubject() != null) {
                msg.setSubject(eMess.getHeaderSubject(), "UTF-8");
            } else {
                msg.setSubject("");
            }
            msg.setSentDate(new Date());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            String text = eMess.getMessageBody();
            messageBodyPart.setContent(text, EMailConstants.EMAIL_PLAIN_TEXT_CONTENT_TYPE);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            /*
             * attachments to be added
             */
            if (eMess.getEmailAttachments() != null) {
                List<EmailAttachment> fileList = eMess.getEmailAttachments();
                for (EmailAttachment emailAttachment : fileList) {
                    MimeBodyPart attachmenBodyPart = new MimeBodyPart();
                    DataSource source = new AttachmentDataSource(emailAttachment.getData(),
                            FileUtil.getContentTypeByFileName(emailAttachment.getFileName()), emailAttachment.getFileName());
                    attachmenBodyPart.setDataHandler(new DataHandler(source));
                    attachmenBodyPart.setFileName(emailAttachment.getFileName());
                    multipart.addBodyPart(attachmenBodyPart);
                }
            }
            // Put parts in message
            msg.setContent(multipart);

            // Send the mail
            Transport.send(msg);
            log.info("message-id: {}" , msg.getHeader("message-id", ","));
        }
        catch (SendFailedException e) {
            saveEmail(eMess, EmailStatus.FAILED);
            throw new AirbnbException("errors.send.email", e);
        } catch (MessagingException e) {
            saveEmail(eMess, EmailStatus.FAILED);
            throw new AirbnbException("errors.send.email", e);

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public List<EmailType> getEmailTypes() {
        return emailTypeRepository.findAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public Email getEmailById(Integer id) throws AirbnbException {
        Optional<Email> emailMaybe = emailRepository.findById(id);
        if(emailMaybe.isPresent()){
            List<EmailAttachment> attachments = emailAttachmentRepository.getEmailAttachmentsByEmailId(id);
            for (EmailAttachment emailAttachment : attachments) {
                byte[] data = fileService.getFileContentById(BigInteger.valueOf(emailAttachment.getFileReferenceId()));
                emailAttachment.setData(data);
            }
            return emailMaybe.get();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateEmailStatus(Integer id, EmailStatus status) {
        Date dateSent = null;
        if (EmailStatus.SENT.equals(status)) {
            dateSent = new Date();
        }
        emailRepository.updateEmailStatusAndDateSentById(id,status,dateSent);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void resendEmails(List<Integer> emailIds) throws AirbnbException {

    }

    @Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
    @Override
    public EmailType getEmailTypeByKey(String resourceKey) {
        return emailTypeRepository.getEmailTypeByKey(resourceKey);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Email saveEmail(Email email, EmailStatus status) throws AirbnbException {
        email.setStatus(status);
        email.setDateCreated(new Date());
        if (EmailStatus.SENT.equals(email.getStatus())) {
            email.setDateSent(new Date());
        }
        if(!CollectionUtils.isEmpty(email.getEmailAttachments())){
            for(EmailAttachment att : email.getEmailAttachments()){
                BigInteger fileId = fileService.createFile(att.getData(), new FileInfo(att.getFileName(), FileUtil.getContentTypeByFileName(att.getFileName()),
                        FileConstants.EMAIL_ATTACHMENT, att.getData().length));
                att.setFileReferenceId(fileId.longValue());
                att.setEmail(email);
            }
        }
        try {
            return emailRepository.save(email);
        } catch (Exception e) {
            email.setStatus(EmailStatus.FAILED);
            emailRepository.save(email);
            throw new AirbnbException("errors.saving.email", e);
        }

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
    public SearchResults<Email> findEmails(EmailSearchRequestDTO searchRequest) {
        return null;
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

    protected MimeMessage toMimeMessage(Email mail)throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("skarandanis@gmail.com", mail.getHeaderFrom());
        messageHelper.setTo(mail.getHeaderTo());
        messageHelper.setSubject(mail.getHeaderSubject());
        messageHelper.setText(mail.getMessageBody(), true);
        return message;
    }
}
