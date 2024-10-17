package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.shared.exception.NotFoundException;

import java.util.List;
import java.util.UUID;

public interface BookingService {

    public Booking create(NewBookingDTO newBookingDTO, String loggedInUserId) throws NotFoundException;

    public List<BookedDateDTO> checkAvailability(String publicId);

    public List<BookedListingDTO> getBookedListing(String loggedInUserId);

    public void cancel(String bookingPublicId, String listingPublicId, boolean byLandlord,String loggedInUserId);

    public List<BookedListingDTO> getBookedListingForLandlord(String loggedInUserId);

    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, String startDate,String endDate);
}
