package com.solon.airbnb.listing.application.dto;

import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.application.dto.vo.PriceVO;

import java.util.UUID;

public record DisplayCardListingDTO(PriceVO price,
                                    String location,
                                    PictureDTO cover,
                                    BookingCategory bookingCategory,
                                    UUID publicId) {
}
