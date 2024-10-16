package com.solon.airbnb.booking.mapper;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking newBookingToBooking(NewBookingDTO newBookingDTO);

    @Mapping(target = "startDate", source = "booking", qualifiedByName = "startDate")
    @Mapping(target = "endDate", source = "booking", qualifiedByName = "endDate")
    BookedDateDTO bookingToCheckAvailability(Booking booking);

    @Named("startDate")
    default String mapStartDate(Booking booking){
        return booking.getStartDate().toString();
    }

    @Named("endDate")
    default String mapEndDate(Booking booking){
        return booking.getEndDate().toString();
    }
}
