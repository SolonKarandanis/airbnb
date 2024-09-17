package com.solon.airbnb.listing.application.dto;

import com.solon.airbnb.listing.application.dto.vo.PriceVO;

import java.util.UUID;

public record ListingCreateBookingDTO(
        UUID listingPublicId, PriceVO price) {
}
