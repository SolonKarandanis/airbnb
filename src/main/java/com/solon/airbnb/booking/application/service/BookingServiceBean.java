package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.shared.service.State;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookingServiceBean implements BookingService{


    @Override
    public State<Void, String> create(NewBookingDTO newBookingDTO) {
        return null;
    }

    @Override
    public List<BookedDateDTO> checkAvailability(UUID publicId) {
        return List.of();
    }

    @Override
    public List<BookedListingDTO> getBookedListing() {
        return List.of();
    }

    @Override
    public State<UUID, String> cancel(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord) {
        return null;
    }

    @Override
    public List<BookedListingDTO> getBookedListingForLandlord() {
        return List.of();
    }

    @Override
    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, String startDate,String endDate) {
        return List.of();
    }
}
