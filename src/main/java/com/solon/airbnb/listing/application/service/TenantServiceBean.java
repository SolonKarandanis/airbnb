package com.solon.airbnb.listing.application.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.ListingSearchRequestDTO;
import com.solon.airbnb.listing.application.dto.sub.LandlordListingDTO;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.listing.domain.Listing;
import com.solon.airbnb.listing.domain.QListing;
import com.solon.airbnb.listing.mapper.ListingMapper;
import com.solon.airbnb.listing.repository.ListingRepository;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TenantServiceBean extends GenericServiceBean implements TenantService{

    private static final Logger log = LoggerFactory.getLogger(TenantServiceBean.class);
    protected static final String USER_NOT_FOUND="error.user.not.found";

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserService userService;
    private final BookingService bookingService;

    public TenantServiceBean(ListingRepository listingRepository, ListingMapper listingMapper, UserService userService, BookingService bookingService) {
        this.listingRepository = listingRepository;
        this.listingMapper = listingMapper;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @Override
    public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
        Page<Listing> allOrBookingCategory;
        if (category == BookingCategory.ALL) {
            allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
        } else {
            allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(pageable, category);
        }
        return allOrBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);
    }

    @Override
    public DisplayListingDTO getOne(String publicId) throws NotFoundException {
        Optional<Listing> listingByPublicIdOpt = listingRepository.findByPublicId(UUID.fromString(publicId));
        if (listingByPublicIdOpt.isEmpty()){
            throw new NotFoundException("error.listing.not.found");
        }
        Listing listing = listingByPublicIdOpt.get();
        DisplayListingDTO displayListingDTO = listingMapper.listingToDisplayListingDTO(listing);
        User landlord = userService.getByPublicId(listing.getLandlordPublicId().toString())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        LandlordListingDTO landlordListingDTO = new LandlordListingDTO(landlord.getFirstName(), landlord.getImageUrl());
        displayListingDTO.setLandlord(landlordListingDTO);
        return displayListingDTO;
    }

    @Override
    public Page<DisplayCardListingDTO> search(ListingSearchRequestDTO searchObj) {
        PageRequest pageRequest = toPageRequest(searchObj.getPaging());
        String startDate = searchObj.getStartDate();
        String endDate = searchObj.getEndDate();
        Predicate searchPredicate = getListingPredicate(searchObj);
        Page<Listing> allMatchedListings =listingRepository.findAll(searchPredicate,pageRequest);
        log.info("TenantServiceBean --> search---> listings found: {}",allMatchedListings.getContent().size());
        List<UUID> listingUUIDs = allMatchedListings.stream()
                .map(Listing::getPublicId)
                .toList();
        List<UUID> bookingUUIDs = bookingService.getBookingMatchByListingIdsAndBookedDate(listingUUIDs, startDate, endDate);
        log.info("TenantServiceBean --> search---> bookings found: {}",bookingUUIDs.size());
        List<DisplayCardListingDTO> listingsNotBooked = allMatchedListings.stream().filter(listing -> !bookingUUIDs.contains(listing.getPublicId()))
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();
        log.info("TenantServiceBean --> search---> listings not booked: {}",listingsNotBooked.size());
        return new PageImpl<>(listingsNotBooked, pageRequest, listingsNotBooked.size());
    }

    protected Predicate getListingPredicate(ListingSearchRequestDTO searchObj){
        QListing listing = QListing.listing;
        BooleanBuilder builder = new BooleanBuilder();

        Integer baths  = searchObj.getBaths().value();
        Integer bedrooms = searchObj.getBedrooms().value();
        Integer guests = searchObj.getGuests().value();
        Integer beds = searchObj.getBeds().value();
        String location = searchObj.getLocation();

        if(Objects.nonNull(baths) && baths >0){
            builder.and(listing.bathrooms.eq(baths));
        }

        if(Objects.nonNull(bedrooms) && bedrooms >0){
            builder.and(listing.bedrooms.eq(bedrooms));
        }

        if(Objects.nonNull(guests) && guests >0){
            builder.and(listing.guests.eq(guests));
        }

        if(Objects.nonNull(beds)  && beds >0){
            builder.and(listing.beds.eq(beds));
        }

        if(StringUtils.hasLength(location)){
            builder.and(listing.location.like(location));
        }
        return builder;
    }
}
