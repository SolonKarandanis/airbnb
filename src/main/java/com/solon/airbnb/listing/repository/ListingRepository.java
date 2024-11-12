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
	
	@Query(name = Listing.FIND_ALL_BY_LANDLORD_PUBLIC_ID_FETCH_COVER_PICTURE)
	List<Listing> findAllByLandlordPublicIdFetchCoverPicture(UUID landlordPublicId);

	@Query(name = Listing.EXISTS_BY_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID)
	Boolean existsByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	@Query(name = Listing.FIND_BY_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID)
	Listing findByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	@Modifying
	@Query(name = Listing.DELETE_BY_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID)
	void deleteByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	@Query(name = Listing.FIND_ALL_BY_BOOKING_CATEGORY_WITH_COVER_ONLY)
	Page<Listing> findAllByBookingCategoryWithCoverOnly(Pageable pageable, BookingCategory bookingCategory);

	@Query(name = Listing.FIND_ALL_WITH_COVER_ONLY)
	Page<Listing> findAllWithCoverOnly(Pageable pageable);

	@Query(name = Listing.FIND_BY_PUBLIC_ID)
	Optional<Listing> findByPublicId(UUID publicId);

	@Query(name = Listing.FIND_ALL_BY_PUBLIC_ID_IN)
	List<Listing> findAllByPublicIdIn(List<UUID> allListingPublicIDs);

	@Query(name = Listing.FIND_BY_PUBLIC_ID_AND_LANDLORD_PUBLIC_ID)
	Optional<Listing> findOneByPublicIdAndLandlordPublicId(UUID publicId, UUID landlordPublicId);

	Page<Listing> findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(
	  Pageable pageable, String location, int bathrooms, int bedrooms, int guests, int beds
	);
}
