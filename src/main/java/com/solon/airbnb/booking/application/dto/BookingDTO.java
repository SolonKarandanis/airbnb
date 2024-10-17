package com.solon.airbnb.booking.application.dto;

public record BookingDTO(
        String publicId,
        String startDate,
        String endDate,
        Integer totalPrice,
        Integer numberOfTravelers,
        String fkTenant,
        String fkListing
) {
}
