package com.solon.airbnb.listing.application.service;

import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.TenantSearchRequestDTO;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.shared.service.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TenantService {

    public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category);

    public State<DisplayListingDTO, String> getOne(String publicId);

    public Page<DisplayCardListingDTO> search(TenantSearchRequestDTO searchObj);
}
