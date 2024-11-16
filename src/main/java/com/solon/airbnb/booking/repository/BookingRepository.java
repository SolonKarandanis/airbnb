package com.solon.airbnb.booking.repository;

import com.solon.airbnb.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(name = Booking.BOOKING_EXISTS_AT_INTERVAL)
    boolean bookingExistsAtInterval(OffsetDateTime startDate, OffsetDateTime endDate, UUID fkListing);

    @Query(name = Booking.FIND_ALL_BY_FK_LISTING)
    List<Booking> findAllByFkListing(UUID fkListing);

    @Query(name = Booking.FIND_ALL_BY_FK_TENANT)
    List<Booking> findAllByFkTenant(UUID fkTenant);

    @Modifying
    int deleteBookingByFkTenantAndPublicId(UUID tenantPublicId, UUID bookingPublicId);

    @Modifying
    int deleteBookingByPublicIdAndFkListing(UUID bookingPublicId, UUID listingPublicId);

    @Query(name = Booking.FIND_ALL_BY_FK_LISTING_IN)
    List<Booking> findAllByFkListingIn(List<UUID> allPropertyPublicIds);

    @Query(name = Booking.FIND_ALL_MATCH_WITH_DATE)
    List<Booking> findAllMatchWithDate(List<UUID> fkListings, OffsetDateTime startDate, OffsetDateTime endDate);
}
