package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.mapper.BookingMapper;
import com.solon.airbnb.booking.repository.BookingRepository;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.shared.service.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookingServiceBean implements BookingService{
    private static final Logger log = LoggerFactory.getLogger(BookingServiceBean.class);

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final LandlordService landlordService;

    public BookingServiceBean(BookingRepository bookingRepository, BookingMapper bookingMapper, LandlordService landlordService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.landlordService = landlordService;
    }

    @Transactional
    @Override
    public State<Void, String> create(NewBookingDTO newBookingDTO, String loggedInUserId) {
        return null;
    }

    @Override
    public List<BookedDateDTO> checkAvailability(String publicId) {
        return List.of();
    }

    @Override
    public List<BookedListingDTO> getBookedListing(String loggedInUserId) {
        return List.of();
    }

    @Transactional
    @Override
    public State<UUID, String> cancel(String bookingPublicId, String listingPublicId, boolean byLandlord,String loggedInUserId) {
        return null;
    }

    @Override
    public List<BookedListingDTO> getBookedListingForLandlord(String loggedInUserId) {
        return List.of();
    }

    @Override
    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, String startDate,String endDate) {
        return List.of();
    }
}
