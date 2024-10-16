package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.shared.service.State;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    public State<Void, String> create(NewBookingDTO newBookingDTO);

    public List<BookedDateDTO> checkAvailability(UUID publicId);

    public List<BookedListingDTO> getBookedListing();

    public State<UUID, String> cancel(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord);

    public List<BookedListingDTO> getBookedListingForLandlord();

    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, String startDate,String endDate);
}
