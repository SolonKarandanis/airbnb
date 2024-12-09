package com.solon.airbnb.booking.controller;

import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.BookingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("BookingControllerTest")
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @InjectMocks
    protected BookingController controller;

    @Mock
    protected BookingService bookingService;

    @Mock
    protected UserService usersService;

    @Mock
    protected Authentication authentication;

    protected BookedListingDTO dto;
    protected User user;
    protected UserDTO userDto;


    @BeforeEach
    public void setup(){
        dto = TestUtil.generateBookedListingDTO();
        user = TestUtil.createTestUser(1L);
        userDto = TestUtil.createTestUserDto(1L);
    }

    @DisplayName("Create Booking")
//    @Test
    void testCreateBooking() throws AirbnbException {
        NewBookingDTO dto = TestUtil.generateNewBookingDTO();
        OffsetDateTime startDate = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime endDate = OffsetDateTime.now(ZoneOffset.UTC);
        Booking booking = TestUtil.generateBooking(startDate,endDate);
        BookingDTO bookingDto = TestUtil.generateBookingDTO(startDate.toString(),endDate.toString());
        when(authentication.getPrincipal()).thenReturn(userDto);
        when(usersService.getByPublicId(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(Optional.of(user));
        when(bookingService.create(dto,user.getPublicId().toString())).thenReturn(booking);
        when(bookingService.convertToDTO(booking)).thenReturn(bookingDto);

        ResponseEntity<BookingDTO> resp = controller.createBooking(dto,authentication);
        assertNotNull(resp);
        assertNotNull(resp.getBody());

        verify(usersService, times(1)).getByPublicId(TestConstants.TEST_USER_PUBLIC_ID);
        verify(bookingService,times(1)).create(dto,user.getPublicId().toString());
        verify(bookingService,times(1)).convertToDTO(booking);
    }
}
