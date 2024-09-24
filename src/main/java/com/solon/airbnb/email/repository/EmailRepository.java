package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.Email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Email email set email.status = :status, email.dateSent = :dateSent where email.id = :emailId")
    public void updateEmailStatusAndDateSentById(Integer emailId, String status, Date dateSent);
}