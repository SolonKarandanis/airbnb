package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.domain.Listing;

import java.util.List;

public interface PictureService {

    public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing);
}
