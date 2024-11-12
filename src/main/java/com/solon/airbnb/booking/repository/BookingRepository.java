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

    List<Booking> findAllByFkListing(UUID fkListing);

    List<Booking> findAllByFkTenant(UUID fkTenant);

    @Modifying
    int deleteBookingByFkTenantAndPublicId(UUID tenantPublicId, UUID bookingPublicId);

    @Modifying
    int deleteBookingByPublicIdAndFkListing(UUID bookingPublicId, UUID listingPublicId);

    List<Booking> findAllByFkListingIn(List<UUID> allPropertyPublicIds);

    @Query("SELECT booking FROM Booking booking WHERE " +
            "NOT (booking.endDate <= :startDate or booking.startDate >= :endDate) " +
            "AND booking.fkListing IN :fkListings")
    List<Booking> findAllMatchWithDate(List<UUID> fkListings, OffsetDateTime startDate, OffsetDateTime endDate);
}
