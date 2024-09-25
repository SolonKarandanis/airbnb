package com.solon.airbnb.email.repository;

import com.solon.airbnb.email.domain.EmailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTypeRepository extends JpaRepository<EmailType, Integer> {

    @Query("SELECT emailType FROM EmailType emailType "
            + "WHERE emailType.resourceKey= :resourceKey ")
    public EmailType getEmailTypeByKey(@Param("resourceKey") String resourceKey);
}
