package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.ListingCreateBookingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.domain.Listing;
import com.solon.airbnb.listing.mapper.ListingMapper;
import com.solon.airbnb.listing.repository.ListingRepository;
import com.solon.airbnb.shared.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class LandlordServiceBean implements LandlordService{

    private static final Logger log = LoggerFactory.getLogger(LandlordServiceBean.class);

    private final ListingRepository listingRepository;
    private final PictureService pictureService;
    private final ListingMapper listingMapper;

    public LandlordServiceBean(
            ListingRepository listingRepository,
            PictureService pictureService,
            ListingMapper listingMapper) {
        this.listingRepository = listingRepository;
        this.pictureService = pictureService;
        this.listingMapper = listingMapper;
    }

    @Transactional
    @Override
    public CreatedListingDTO create(String userPublicId,SaveListingDTO saveListingDTO, List<PictureDTO> pictures) {
        Listing newListing = listingMapper.saveListingDTOToListing(saveListingDTO);
        newListing.setLandlordPublicId(UUID.fromString(userPublicId));
        Listing savedListing = listingRepository.saveAndFlush(newListing);
        pictureService.saveAll(pictures, savedListing);
        return listingMapper.listingToCreatedListingDTO(savedListing);
    }

    @Override
    public List<DisplayCardListingDTO> getAllProperties(String landlordId) {
        List<Listing> properties = listingRepository.findAllByLandlordPublicIdFetchCoverPicture(UUID.fromString(landlordId));
        return listingMapper.listingToDisplayCardListingDTOs(properties);
    }

    @Transactional
    @Override
    public void delete(String publicId,String landlordPublicId) throws NotFoundException {
        Boolean exists = listingRepository.existsByPublicIdAndLandlordPublicId(UUID.fromString(publicId),UUID.fromString(landlordPublicId));
        if(!exists){
            throw new NotFoundException("error.listing.not.found");
        }
        listingRepository.deleteByPublicIdAndLandlordPublicId(UUID.fromString(publicId),UUID.fromString(landlordPublicId));
    }

    @Override
    public Optional<ListingCreateBookingDTO> getByListingPublicId(String publicId) {
        return listingRepository.findByPublicId(UUID.fromString(publicId)).map(listingMapper::mapListingToListingCreateBookingDTO);
    }

    @Override
    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<UUID> allListingPublicIDs) {
        return listingRepository.findAllByPublicIdIn(allListingPublicIDs)
                .stream()
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();
    }

    @Override
    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(String listingPublicId, String landlordPublicId) {
        return listingRepository.findOneByPublicIdAndLandlordPublicId(UUID.fromString(listingPublicId), UUID.fromString(landlordPublicId))
                .map(listingMapper::listingToDisplayCardListingDTO);
    }
}
