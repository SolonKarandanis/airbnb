package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.EmailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailAttachmentRepository extends JpaRepository<EmailAttachment, Long> {

    @Query("SELECT ea FROM EmailAttachment ea "
            + "WHERE ea.emailsId= :emailsId ")
    public List<EmailAttachment> getEmailAttachmentsByEmailId(@Param("emailsId") Long emailsId);
}
