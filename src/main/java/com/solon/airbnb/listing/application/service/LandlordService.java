package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.shared.service.State;
import com.solon.airbnb.user.application.dto.ReadUserDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LandlordService {

    public CreatedListingDTO create(SaveListingDTO saveListingDTO);

    public List<DisplayCardListingDTO> getAllProperties(ReadUserDTO landlord);

    public State<UUID, String> delete(UUID publicId, ReadUserDTO landlord);

    public Optional<ListingCreateBookingDTO> getByListingPublicId(UUID publicId);

    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<UUID> allListingPublicIDs);

    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId);
}
