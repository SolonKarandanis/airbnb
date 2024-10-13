package com.solon.airbnb.email.domain;

import com.solon.airbnb.shared.domain.DomainConstants;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "email_attachments")
public class EmailAttachment {

    @Id
    @SequenceGenerator(name = DomainConstants.EMAIL_ATTACHMENT_GEN, sequenceName = DomainConstants.EMAIL_ATTACHMENT_SQ, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DomainConstants.EMAIL_ATTACHMENT_GEN)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailAttachment that = (EmailAttachment) o;
        return Objects.equals(fileName, that.fileName) &&
                Objects.equals(fileReferenceId, that.fileReferenceId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileReferenceId);
    }
}
