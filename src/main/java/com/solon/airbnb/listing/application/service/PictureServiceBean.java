package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.domain.Listing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PictureServiceBean implements PictureService{

    @Override
    public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing) {
        return List.of();
    }
}
