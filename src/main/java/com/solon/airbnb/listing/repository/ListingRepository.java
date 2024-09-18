package com.solon.airbnb.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.listing.domain.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>{

}
