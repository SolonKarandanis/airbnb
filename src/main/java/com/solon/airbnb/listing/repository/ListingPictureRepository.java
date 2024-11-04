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
    @Query("DELETE FROM ListingPicture lp " +
            "WHERE lp.id = (SELECT l.id FROM Listing l WHERE l.publicId = :publicId AND l.landlordPublicId = :landlordPublicId)")
    void deleteByListingPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);
}
