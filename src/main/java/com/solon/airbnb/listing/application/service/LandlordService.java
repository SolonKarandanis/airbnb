package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.shared.exception.NotFoundException;
import com.solon.airbnb.user.application.dto.ReadUserDTO;

import java.util.List;
import java.util.Optional;

public interface LandlordService {

    public CreatedListingDTO create(String userPublicId,SaveListingDTO saveListingDTO);

    public List<DisplayCardListingDTO> getAllProperties(String landlordId);

    public void delete(String publicId, ReadUserDTO landlord) throws NotFoundException;

    public Optional<ListingCreateBookingDTO> getByListingPublicId(String publicId);

    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<String> allListingPublicIDs);

    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(String listingPublicId, String landlordPublicId);
}
