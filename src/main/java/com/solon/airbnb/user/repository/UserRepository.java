package com.solon.airbnb.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByPublicId(UUID publicId);
    
    @Query("SELECT u FROM Users u "
            + "JOIN FETCH u.authorities a "
            + "WHERE u.username= :username ")
    Optional<User> findByUsername(@Param("username")String username);
}
