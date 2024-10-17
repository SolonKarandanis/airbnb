package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.shared.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LandlordService {

    public CreatedListingDTO create(String userPublicId,SaveListingDTO saveListingDTO);

    public List<DisplayCardListingDTO> getAllProperties(String landlordId);

    public void delete(String publicId, String landlordPublicId) throws NotFoundException;

    public Optional<ListingCreateBookingDTO> getByListingPublicId(String publicId);

    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<UUID> allListingPublicIDs);

    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(String listingPublicId, String landlordPublicId);
}
