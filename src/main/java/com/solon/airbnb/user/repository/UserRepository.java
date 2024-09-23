package com.solon.airbnb.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> ,JpaSpecificationExecutor<User>{

    Optional<User> findOneByEmail(String email);

    Optional<User> findOneByPublicId(UUID publicId);
    
    @Query("SELECT u FROM User u "
            + "LEFT JOIN FETCH u.authorities a "
            + "WHERE u.username= :username ")
    Optional<User> findByUsername(@Param("username")String username);
    
    @EntityGraph(value =  User.GRAPH_USERS_AUTHORITIES,type = EntityGraphType.FETCH)
    @Query("SELECT u FROM User u ")
    List<User> findUsersWithRoles(Specification<User> spec);
}
