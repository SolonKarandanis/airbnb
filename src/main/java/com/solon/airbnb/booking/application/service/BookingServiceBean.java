package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.booking.mapper.BookingMapper;
import com.solon.airbnb.booking.repository.BookingRepository;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.vo.PriceVO;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.shared.service.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
    public Booking create(NewBookingDTO newBookingDTO, String loggedInUserId) throws NotFoundException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss Z");
        Booking booking = bookingMapper.newBookingToBooking(newBookingDTO,formatter);

        Optional<ListingCreateBookingDTO> listingOpt = landlordService.getByListingPublicId(newBookingDTO.listingPublicId());
        if (listingOpt.isEmpty()){
            throw new NotFoundException("error.landlord.not.found");
        }
        boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(
                OffsetDateTime.parse(newBookingDTO.startDate(),formatter),
                OffsetDateTime.parse(newBookingDTO.endDate(),formatter),
                UUID.fromString(newBookingDTO.listingPublicId()));

        if (alreadyBooked){
            throw new NotFoundException("error.booking.exists");
        }

        ListingCreateBookingDTO listingCreateBookingDTO = listingOpt.get();
        booking.setFkListing(listingCreateBookingDTO.listingPublicId());
        booking.setFkTenant(UUID.fromString(loggedInUserId));
        booking.setNumberOfTravelers(1);
        long numberOfNights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        booking.setTotalPrice((int) (numberOfNights * listingCreateBookingDTO.price().value()));

        booking=bookingRepository.save(booking);
        return booking;
    }

    @Override
    public List<BookedDateDTO> checkAvailability(String publicId) {
        return bookingRepository.findAllByFkListing(UUID.fromString(publicId))
                .stream().map(bookingMapper::bookingToCheckAvailability).toList();
    }

    @Override
    public List<BookedListingDTO> getBookedListing(String loggedInUserId) {
        List<Booking> allBookings = bookingRepository.findAllByFkTenant(UUID.fromString(loggedInUserId));
        List<UUID> allListingPublicIDs = allBookings.stream().map(Booking::getFkListing).toList();
        List<DisplayCardListingDTO> allListings = landlordService.getCardDisplayByListingPublicId(allListingPublicIDs);
        return mapBookingToBookedListing(allBookings, allListings);
    }

    private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBookings, List<DisplayCardListingDTO> allListings) {
        return allBookings.stream().map(booking -> {
            DisplayCardListingDTO displayCardListingDTO = allListings
                    .stream()
                    .filter(listing -> listing.publicId().equals(booking.getFkListing()))
                    .findFirst()
                    .orElseThrow();
            BookedDateDTO dates = bookingMapper.bookingToCheckAvailability(booking);
            return new BookedListingDTO(displayCardListingDTO.cover(),
                    displayCardListingDTO.location(),
                    dates, new PriceVO(booking.getTotalPrice()),
                    booking.getPublicId(), displayCardListingDTO.publicId());
        }).toList();
    }

    @Transactional
    @Override
    public void cancel(String bookingPublicId, String listingPublicId, boolean byLandlord,String loggedInUserId) {

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
