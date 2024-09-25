package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.Email;

import com.solon.airbnb.email.domain.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Email email set email.status = :status, email.dateSent = :dateSent where email.id = :emailId")
    public void updateEmailStatusAndDateSentById(Integer emailId, EmailStatus status, Date dateSent);
}
