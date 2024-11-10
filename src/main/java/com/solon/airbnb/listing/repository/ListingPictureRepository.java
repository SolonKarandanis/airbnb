package com.solon.airbnb.listing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.listing.domain.ListingPicture;

import java.util.UUID;

@Repository
public interface ListingPictureRepository extends JpaRepository<ListingPicture, Long> {

    @Modifying
    @Query(name = ListingPicture.DELETE_BY_LISTING_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID)
    void deleteByListingPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);
}
