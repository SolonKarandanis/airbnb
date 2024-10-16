package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.TenantSearchRequestDTO;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.listing.domain.Listing;
import com.solon.airbnb.listing.mapper.ListingMapper;
import com.solon.airbnb.listing.repository.ListingRepository;
import com.solon.airbnb.shared.service.GenericServiceBean;
import com.solon.airbnb.shared.service.State;
import com.solon.airbnb.user.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TenantServiceBean extends GenericServiceBean implements TenantService{

    private static final Logger log = LoggerFactory.getLogger(TenantServiceBean.class);

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
    public State<DisplayListingDTO, String> getOne(String publicId) {
        return null;
    }

    @Override
    public Page<DisplayCardListingDTO> search(TenantSearchRequestDTO searchObj) {
        return null;
    }
}
