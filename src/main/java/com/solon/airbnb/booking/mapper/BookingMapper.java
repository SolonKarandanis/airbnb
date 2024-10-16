package com.solon.airbnb.booking.mapper;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.shared.utils.DateUitl;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "startDate", source = "newBookingDTO", qualifiedByName = "startDate")
    @Mapping(target = "endDate", source = "newBookingDTO", qualifiedByName = "endDate")
    Booking newBookingToBooking(NewBookingDTO newBookingDTO);

    @Named("startDate")
    default OffsetDateTime mapStartDate(NewBookingDTO newBookingDTO){
        return  DateUitl.convertFromStringToOffsetDateTime(newBookingDTO.startDate());
    }

    @Named("endDate")
    default OffsetDateTime mapEndDate(NewBookingDTO newBookingDTO){
        return  DateUitl.convertFromStringToOffsetDateTime(newBookingDTO.endDate());
    }

    @Mapping(target = "startDate", source = "booking", qualifiedByName = "startStringDate")
    @Mapping(target = "endDate", source = "booking", qualifiedByName = "endStringDate")
    BookedDateDTO bookingToCheckAvailability(Booking booking);

    @Named("startStringDate")
    default String mapStringStartDate(Booking booking){
        return booking.getStartDate().toString();
    }

    @Named("endStringDate")
    default String mapStringEndDate(Booking booking){
        return booking.getEndDate().toString();
    }

    @Mapping(target = "startDate", source = "booking", qualifiedByName = "startStringDate")
    @Mapping(target = "endDate", source = "booking", qualifiedByName = "endStringDate")
    BookingDTO bookingToBookingDTO(Booking booking);
}
