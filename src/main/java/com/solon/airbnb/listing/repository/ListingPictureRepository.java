package com.solon.airbnb.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.listing.domain.ListingPicture;

@Repository
public interface ListingPictureRepository extends JpaRepository<ListingPicture, Long> {
	
	

  
}
