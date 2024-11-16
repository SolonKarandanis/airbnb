package com.solon.airbnb.booking.application.service;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.BookingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.booking.mapper.BookingMapper;
import com.solon.airbnb.booking.repository.BookingRepository;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.vo.PriceVO;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.shared.common.AuthorityConstants;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.shared.utils.DateUitl;
import com.solon.airbnb.user.application.utils.UserUtil;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        Booking booking = bookingMapper.newBookingToBooking(newBookingDTO);

        Optional<ListingCreateBookingDTO> listingOpt = landlordService.getByListingPublicId(newBookingDTO.listingPublicId());
        if (listingOpt.isEmpty()){
            throw new NotFoundException("error.landlord.not.found");
        }
        boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(
                DateUitl.convertFromStringToOffsetDateTime(newBookingDTO.startDate()),
                DateUitl.convertFromStringToOffsetDateTime(newBookingDTO.endDate()),
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
    public List<BookedListingDTO> getBookedListings(String loggedInUserId) {
        List<Booking> allBookings = bookingRepository.findAllByFkTenant(UUID.fromString(loggedInUserId));
        List<UUID> allListingPublicIDs = allBookings.stream().map(Booking::getFkListing).toList();
        List<DisplayCardListingDTO> allListings = landlordService.getCardDisplayByListingPublicId(allListingPublicIDs);
        return mapBookingToBookedListing(allBookings, allListings);
    }

    private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBookings, List<DisplayCardListingDTO> allListings) {
        List<BookedListingDTO> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(allBookings) && CollectionUtils.isEmpty(allListings)){
            return new ArrayList<>();
        }
        for(Booking booking : allBookings){
            Optional<DisplayCardListingDTO> displayCardListingDTOOpt = allListings.stream()
                    .filter(listing -> listing.publicId().equals(booking.getFkListing().toString()))
                    .findFirst();
            BookedDateDTO  dates = bookingMapper.bookingToCheckAvailability(booking);
            displayCardListingDTOOpt.ifPresent(display->{
                BookedListingDTO dto = new BookedListingDTO(display.cover(),
                        display.location(),
                        dates, new PriceVO(booking.getTotalPrice()),
                        booking.getPublicId().toString(),display.publicId());
                result.add(dto);
            });
        }
        return result;
    }

    @Transactional
    @Override
    public void cancel(String bookingPublicId, String listingPublicId, boolean byLandlord, User loggedInUser)
            throws NotFoundException{
        int deleteSuccess = 0;
        if(UserUtil.hasAuthority(loggedInUser,AuthorityConstants.ROLE_LANDLORD)){
            Optional<DisplayCardListingDTO> listingVerificationOpt = landlordService
                    .getByPublicIdAndLandlordPublicId(listingPublicId, loggedInUser.getPublicId().toString());
            if (listingVerificationOpt.isPresent()){
                deleteSuccess = bookingRepository.deleteBookingByPublicIdAndFkListing(UUID.fromString(bookingPublicId), UUID.fromString(listingVerificationOpt.get().publicId()));
            }
        }
        else{
            deleteSuccess = bookingRepository.deleteBookingByFkTenantAndPublicId(loggedInUser.getPublicId(), UUID.fromString(bookingPublicId));
        }
        if(deleteSuccess ==0){
            throw new NotFoundException("error.booking.not.found");
        }
    }

    @Override
    public List<BookedListingDTO> getBookedListingsForLandlord(String loggedInUserId) {
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(loggedInUserId);
        List<UUID> allPropertyPublicIds = allProperties.stream().map(DisplayCardListingDTO::publicId).map(UUID::fromString).toList();
        List<Booking> allBookings = bookingRepository.findAllByFkListingIn(allPropertyPublicIds);
        return mapBookingToBookedListing(allBookings, allProperties);
    }

    @Override
    public List<UUID> getBookingMatchByListingIdsAndBookedDate(List<UUID> listingsId, String startDate,String endDate) {
        return bookingRepository.findAllMatchWithDate(
                listingsId, DateUitl.convertFromStringToOffsetDateTime(startDate), DateUitl.convertFromStringToOffsetDateTime(endDate))
                .stream().map(Booking::getFkListing).toList();
    }

    @Override
    public BookingDTO convertToDTO(Booking booking) {
        return bookingMapper.bookingToBookingDTO(booking);
    }
}
