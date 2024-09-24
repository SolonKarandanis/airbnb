package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.Email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
}
