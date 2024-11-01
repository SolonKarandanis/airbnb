package com.solon.airbnb.listing.controller;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.user.application.exception.UserException;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
        log.info("LandlordController->getAllListings->user: {}" , loggedInUserId);
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(loggedInUserId);
        return ResponseEntity.ok(allProperties);
    }

    @DeleteMapping("/listings/{id}")
    public ResponseEntity<Void> deleteListing(
            @PathVariable(name= "id",required=true) @Min(1) String listingPublicId,
            Authentication authentication){
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("LandlordController->deleteListing->user: {}" , loggedInUserId);
        landlordService.delete(listingPublicId,loggedInUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping()
    public ResponseEntity<CreatedListingDTO> createListing(
            @RequestPart(name = "images") MultipartFile[] images,
            @RequestPart(name = "dto") SaveListingDTO saveListingDTO,
            Authentication authentication
    ){
        String loggedInUserId = getLoggedInUserUUID(authentication);
        log.info("LandlordController->createListing->user: {}" , loggedInUserId);
        List<MultipartFile> imageList  = Arrays.asList(images);
        List<PictureDTO> pictures = imageList.stream()
                .map(mapMultipartFileToPictureDTO())
                .toList();
        CreatedListingDTO newListing = landlordService.create(loggedInUserId,saveListingDTO,pictures);
        return ResponseEntity.ok(newListing);
    }


    private static Function<MultipartFile, PictureDTO> mapMultipartFileToPictureDTO() {
        return multipartFile -> {
            try {
                return new PictureDTO(multipartFile.getBytes(), multipartFile.getContentType(), false);
            } catch (IOException ioe) {
                throw new UserException(String.format("Cannot parse multipart file: %s", multipartFile.getOriginalFilename()));
            }
        };
    }

}
