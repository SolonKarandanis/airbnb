package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.shared.service.State;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LandlordServiceBean implements LandlordService{


    @Override
    public CreatedListingDTO create(SaveListingDTO saveListingDTO) {
        return null;
    }

    @Override
    public List<DisplayCardListingDTO> getAllProperties(ReadUserDTO landlord) {
        return List.of();
    }

    @Override
    public State<UUID, String> delete(UUID publicId, ReadUserDTO landlord) {
        return null;
    }

    @Override
    public Optional<ListingCreateBookingDTO> getByListingPublicId(UUID publicId) {
        return Optional.empty();
    }

    @Override
    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<UUID> allListingPublicIDs) {
        return List.of();
    }

    @Override
    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId) {
        return Optional.empty();
    }
}
