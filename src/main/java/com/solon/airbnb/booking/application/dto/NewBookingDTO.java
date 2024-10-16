package com.solon.airbnb.booking.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NewBookingDTO(
        @NotNull String startDate,
        @NotNull String endDate,
        @NotNull String listingPublicId) {
}
