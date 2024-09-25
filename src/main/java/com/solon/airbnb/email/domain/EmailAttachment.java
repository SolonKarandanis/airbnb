package com.solon.airbnb.email.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "email_attachments")
public class EmailAttachment {

    @Id
    @SequenceGenerator(name = "EMAIL_ATTACHMENTS_ID_GENERATOR", sequenceName = "email_attachments_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAIL_ATTACHMENTS_ID_GENERATOR")
    private Integer id;


    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_reference_id")
    private Long fileReferenceId;

    @Column(name = "emails_id")
    private Long emailsId;

    //bi-directional many-to-one association to Email
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emails_id",insertable=false, updatable=false)
    private Email email;

    private transient byte[] data;

    public EmailAttachment() {
    }

    public EmailAttachment(String fileName, byte[] data) {
        this.fileName = fileName;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileReferenceId() {
        return fileReferenceId;
    }

    public void setFileReferenceId(Long fileReferenceId) {
        this.fileReferenceId = fileReferenceId;
    }

    public Long getEmailsId() {
        return emailsId;
    }

    public void setEmailsId(Long emailsId) {
        this.emailsId = emailsId;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
