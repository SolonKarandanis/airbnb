package com.solon.airbnb.booking.controller;

import com.solon.airbnb.booking.application.service.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("BookingControllerTest")
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @InjectMocks
    protected BookingController controller;

    @Mock
    protected BookingService bookingService;
}
