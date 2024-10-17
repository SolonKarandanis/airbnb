package com.solon.airbnb.listing.controller;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.shared.controller.GenericController;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/landlord")
public class LandlordController extends GenericController {

    private static final Logger log = LoggerFactory.getLogger(LandlordController.class);

    private final LandlordService landlordService;

    public LandlordController(LandlordService landlordService){
        this.landlordService=landlordService;
    }

    @GetMapping(value = "/listings")
//    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD + "')")
    public ResponseEntity<List<DisplayCardListingDTO>> getAllListings(Authentication authentication){
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("BookingController->getBookedListing->user: {}" , loggedInUserId);
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(loggedInUserId);
        return ResponseEntity.ok(allProperties);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(
            @PathVariable(name= "id",required=true) @Min(1) String listingPublicId,
            Authentication authentication){
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("BookingController->getBookedListing->user: {}" , loggedInUserId);
        landlordService.delete(listingPublicId,loggedInUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<CreatedListingDTO> createListing(
            @RequestPart MultipartFile file,
            @RequestPart(name = "dto") String saveListingDTOString,
            Authentication authentication
            ){
        return null;
    }

}
