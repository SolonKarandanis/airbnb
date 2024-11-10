package com.solon.airbnb.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.user.domain.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,String>{

    @Query(name=Authority.FIND_BY_NAME)
    Authority findByName(String name);
}
