package com.solon.airbnb.email.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "email_types")
public class EmailType {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "resource_key")
    private String resourceKey;

    //bi-directional many-to-one association to Email
    @OneToMany(mappedBy = "emailType")
    private List<Email> emails;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
