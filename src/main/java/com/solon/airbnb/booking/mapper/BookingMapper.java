package com.solon.airbnb.booking.mapper;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking newBookingToBooking(NewBookingDTO newBookingDTO);

    BookedDateDTO bookingToCheckAvailability(Booking booking);
}
