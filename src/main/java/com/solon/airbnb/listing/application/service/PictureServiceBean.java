package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.domain.Listing;
import com.solon.airbnb.listing.domain.ListingPicture;
import com.solon.airbnb.listing.mapper.ListingPictureMapper;
import com.solon.airbnb.listing.repository.ListingPictureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PictureServiceBean implements PictureService{

    private static final Logger log = LoggerFactory.getLogger(PictureServiceBean.class);

    private final ListingPictureRepository listingPictureRepository;
    private final ListingPictureMapper listingPictureMapper;

    public PictureServiceBean(ListingPictureRepository listingPictureRepository, ListingPictureMapper listingPictureMapper) {
        this.listingPictureRepository = listingPictureRepository;
        this.listingPictureMapper = listingPictureMapper;
    }

    @Transactional
    @Override
    public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing) {
        Set<ListingPicture> listingPictures = listingPictureMapper.pictureDTOsToListingPictures(pictures);
        boolean isFirst = true;

        for (ListingPicture listingPicture : listingPictures) {
            listingPicture.setCover(isFirst);
            listingPicture.setListing(listing);
            isFirst = false;
        }
        listingPictureRepository.saveAll(listingPictures);
        return listingPictureMapper.listingPictureToPictureDTO(listingPictures.stream().toList());
    }
}
