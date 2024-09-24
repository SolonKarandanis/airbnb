package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.EmailAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAttachmentRepository extends JpaRepository<EmailAttachment, Long> {
}
