package com.solon.airbnb.listing.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.listing.domain.Listing;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>{
	
	@Query("SELECT listing FROM Listing listing LEFT JOIN FETCH listing.pictures picture" +
	        " WHERE listing.landlordPublicId = :landlordPublicId AND picture.isCover = true")
	List<Listing> findAllByLandlordPublicIdFetchCoverPicture(UUID landlordPublicId);

	Boolean existsByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	Listing findByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	@Modifying
	@Query("DELETE FROM Listing l " +
			"WHERE l.publicId = :publicId " +
			"AND l.landlordPublicId = :landlordPublicId")
	void deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	@Query("SELECT listing from Listing listing LEFT JOIN FETCH listing.pictures picture" +
	        " WHERE picture.isCover = true AND listing.bookingCategory = :bookingCategory")
	Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);

	@Query("SELECT listing from Listing listing LEFT JOIN FETCH listing.pictures picture" +
	        " WHERE picture.isCover = true")
	Page<Listing> findAllWithCoverOnly(Pageable pageable);

	Optional<Listing> findByPublicId(UUID publicId);

	List<Listing> findAllByPublicIdIn(List<UUID> allListingPublicIDs);


	Optional<Listing> findOneByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId);

	Page<Listing> findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(
	  Pageable pageable, String location, int bathrooms, int bedrooms, int guests, int beds
	);
}
