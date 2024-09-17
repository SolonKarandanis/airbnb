package com.solon.airbnb.listing.application.dto;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.listing.application.dto.sub.ListingInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SearchDTO(@Valid BookedDateDTO dates,
                        @Valid ListingInfoDTO infos,
                        @NotEmpty String location) {
}
