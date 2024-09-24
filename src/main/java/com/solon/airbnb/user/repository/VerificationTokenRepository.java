package com.solon.airbnb.user.repository;

import com.solon.airbnb.user.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    public VerificationTokenRepository findByToken(String token);
}
