package com.solon.airbnb.email.application.service;

import com.solon.airbnb.email.application.dto.EmailDTO;
import com.solon.airbnb.email.domain.Email;
import com.solon.airbnb.email.domain.EmailType;
import com.solon.airbnb.shared.exception.AirbnbException;

import java.util.List;
import java.util.Map;

public interface EmailService {
    /**
     * Utilize the JMX Queue to send an email
     * Send emails
     */
    public void saveAndSendEmail(Email eMess) throws AirbnbException;
    /**
     * Sends e-mail as instance of class <code>EMailMessage</code>
     * synchronously.
     * VERY IMPORTANT: SHOULD ONLY BE CALLED FROM AN MDB OR ANOTHER
     * <B>ASYNCHRONOUS</B> MECHANISM!
     *
     * @param eMess
     *            <code>EMailMessage</code>
     *            The message to send. Contains all the necessary
     *            fields.
     * @throws EDException
     *             If there is a problem with the back-end.
     */
    public void saveAndSendEmailSynchronous(Email eMess) throws AirbnbException;
    /**
     * Return all email types used in the application.
     *
     * @return
     */
    List<EmailType> getEmailTypes();
     /**
     * Returns email object along with the email body.
     *
     * @param id
     *            - message id.
     * @return email object along with the email body
     * @throws EDException
     *             If there is a problem with the back-end.
     */
    Email getEmailById(Integer id) throws AirbnbException;
    /**
     * Updates email status.
     *
     * @param id
     * @param status
     */
    void updateEmailStatus(Integer id, String status);
    /**
     * Resends the emails stored in the database.
     *
     * @param emailIds
     *            - list of email ids
     * @throws EDException
     *             If there is a problem with the back-end.
     */
    void resendEmails(List<Integer> emailIds) throws AirbnbException;
    /**
     * Return email type object by resource key.
     *
     * @param resourceKey
     * @return
     */
    EmailType getEmailTypeByKey(String resourceKey);
    /**
     * Saves the email in the database.
     */
    Integer saveEmail(Email email, String status) throws AirbnbException;

    public EmailDTO convertToDTO(Email email, Boolean withMsgBody);

    public Map<String, String> getEmailColumnMap();

    public Map<String, String> getEmailSortingFieldsMap();

}
