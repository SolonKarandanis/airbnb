package com.solon.airbnb.email.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {

    @Id
    @SequenceGenerator(name = "EMAILS_ID_GENERATOR", sequenceName = "email_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAILS_ID_GENERATOR")
    private Integer id;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private Date dateCreated;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_sent")
    private Date dateSent;


    @Column(name = "header_bcc")
    private String headerBcc;

    @Column(name = "header_cc")
    private String headerCc;

    @Column(name = "header_from")
    private String headerFrom;

    @Column(name = "header_reply_to")
    private String headerReplyTo;

    @Column(name = "header_subject")
    private String headerSubject;

    @Column(name = "header_to")
    private String headerTo;

    @Column(name = "status")
    private EmailStatus status;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "message_body")
    private String messageBody;

    @Column(name = "email_types_id")
    private Long emailTypesId;

    //bi-directional many-to-one association to EmailType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_types_id",insertable=false, updatable=false)
    private EmailType emailType;

    //bi-directional many-to-one association to EmailAttachment
    @OneToMany(mappedBy = "email", cascade = CascadeType.PERSIST)
    private List<EmailAttachment> emailAttachments;


    @Column(name = "details_1")
    private String details1;

    public Email() {
    }

    public Email(String headerFrom, String headerTo, String headerCc, String headerBcc, String headerSubject, String messageBody,
                 List<EmailAttachment> emailAttachments) {
        this.headerFrom = headerFrom;
        this.headerTo = headerTo;
        this.headerSubject = headerSubject;
        this.messageBody = messageBody;
        this.headerCc = headerCc;
        this.headerBcc = headerBcc;
        this.emailAttachments = emailAttachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public String getHeaderBcc() {
        return headerBcc;
    }

    public void setHeaderBcc(String headerBcc) {
        this.headerBcc = headerBcc;
    }

    public String getHeaderCc() {
        return headerCc;
    }

    public void setHeaderCc(String headerCc) {
        this.headerCc = headerCc;
    }

    public String getHeaderFrom() {
        return headerFrom;
    }

    public void setHeaderFrom(String headerFrom) {
        this.headerFrom = headerFrom;
    }

    public String getHeaderReplyTo() {
        return headerReplyTo;
    }

    public void setHeaderReplyTo(String headerReplyTo) {
        this.headerReplyTo = headerReplyTo;
    }

    public String getHeaderSubject() {
        return headerSubject;
    }

    public void setHeaderSubject(String headerSubject) {
        this.headerSubject = headerSubject;
    }

    public String getHeaderTo() {
        return headerTo;
    }

    public void setHeaderTo(String headerTo) {
        this.headerTo = headerTo;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Long getEmailTypesId() {
        return emailTypesId;
    }

    public void setEmailTypesId(Long emailTypesId) {
        this.emailTypesId = emailTypesId;
    }

    public EmailType getEmailType() {
        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public List<EmailAttachment> getEmailAttachments() {
        return emailAttachments;
    }

    public void setEmailAttachments(List<EmailAttachment> emailAttachments) {
        this.emailAttachments = emailAttachments;
    }

    public String getDetails1() {
        return details1;
    }

    public void setDetails1(String details1) {
        this.details1 = details1;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }
}
