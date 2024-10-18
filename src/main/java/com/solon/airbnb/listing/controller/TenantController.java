package com.solon.airbnb.listing.controller;

import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.TenantSearchRequestDTO;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.shared.controller.GenericController;
import com.solon.airbnb.shared.dto.SearchResults;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/tenant")
public class TenantController extends GenericController {

    private static final Logger log = LoggerFactory.getLogger(TenantController.class);

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/by-category")
    public ResponseEntity<SearchResults<DisplayCardListingDTO>> findAllByBookingCategory(
            Pageable pageable,
            @RequestParam BookingCategory category){
        Page<DisplayCardListingDTO> results = tenantService.getAllByCategory(pageable, category);
        return ResponseEntity.ok().body(new SearchResults<DisplayCardListingDTO>(Math.toIntExact(results.getTotalElements()), results.getContent()));
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResults<DisplayCardListingDTO>> searchListings(@RequestBody @Valid TenantSearchRequestDTO searchObj){
        Page<DisplayCardListingDTO> results = tenantService.search(searchObj);
        return ResponseEntity.ok().body(new SearchResults<DisplayCardListingDTO>(Math.toIntExact(results.getTotalElements()), results.getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayListingDTO> getDisplayListing(@PathVariable(name= "id", required=true) String publicId){
        return ResponseEntity.ok().body(tenantService.getOne(publicId));
    }
}
