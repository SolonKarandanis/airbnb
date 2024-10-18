package com.solon.airbnb.booking.controller;

import com.solon.airbnb.booking.application.dto.BookedDateDTO;
import com.solon.airbnb.booking.application.dto.BookedListingDTO;
import com.solon.airbnb.booking.application.dto.BookingDTO;
import com.solon.airbnb.booking.application.dto.NewBookingDTO;
import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.booking.domain.Booking;
import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.user.domain.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/booking")
public class BookingController extends GenericController {

    private static final Logger log = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody NewBookingDTO newBookingDTO, Authentication authentication)
            throws AirbnbException {
        User user = getLoggedInUser(authentication);
        log.info("BookingController->create->user: {}" , user.getUsername());
        Booking booking=bookingService.create(newBookingDTO,user.getPublicId().toString());
        return ResponseEntity.ok(bookingService.convertToDTO(booking));
    }

    @GetMapping("check-availability")
    public ResponseEntity<List<BookedDateDTO>> checkAvailability(@RequestParam String listingPublicId){
        return ResponseEntity.ok(bookingService.checkAvailability(listingPublicId));
    }

    @GetMapping("booked-listing")
    public ResponseEntity<List<BookedListingDTO>> getBookedListings(Authentication authentication) {
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("BookingController->getBookedListing->user: {}" , loggedInUserId);
        return ResponseEntity.ok(bookingService.getBookedListings(loggedInUserId));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelBooking(
            @RequestParam String bookingPublicId,
            @RequestParam String listingPublicId,
            @RequestParam boolean byLandlord,
            Authentication authentication) throws AirbnbException {
        User user = getLoggedInUser(authentication);
        log.info("BookingController->create->user: {}" , user.getUsername());
        bookingService.cancel(bookingPublicId,listingPublicId,byLandlord,user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("listing-for-landlord")
//    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD + "')")
    public ResponseEntity<List<BookedListingDTO>> getBookedListingForLandlord(Authentication authentication) {
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("BookingController->getBookedListingForLandlord->user: {}" , loggedInUserId);
        return ResponseEntity.ok(bookingService.getBookedListingsForLandlord(loggedInUserId));
    }
}
