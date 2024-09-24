package com.solon.airbnb.user.repository;

import com.solon.airbnb.user.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT vt FROM VerificationToken vt "
            + "LEFT JOIN FETCH vt.user u "
            + "WHERE vt.token= :token ")
    public VerificationToken findByToken(String token);
}
