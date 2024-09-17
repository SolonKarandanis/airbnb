package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.SearchDTO;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.shared.service.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TenantServiceBean implements TenantService{


    @Override
    public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
        return null;
    }

    @Override
    public State<DisplayListingDTO, String> getOne(UUID publicId) {
        return null;
    }

    @Override
    public Page<DisplayCardListingDTO> search(Pageable pageable, SearchDTO newSearch) {
        return null;
    }
}
