package com.solon.airbnb.booking.application.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record BookedDateDTO(
        @NotNull String startDate,
        @NotNull String endDate
) {
}
