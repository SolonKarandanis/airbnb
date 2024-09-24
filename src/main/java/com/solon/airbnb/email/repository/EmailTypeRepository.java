package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTypeRepository extends JpaRepository<EmailType, Long> {
}
