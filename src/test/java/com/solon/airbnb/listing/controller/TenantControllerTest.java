package com.solon.airbnb.listing.controller;


import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayListingDTO;
import com.solon.airbnb.listing.application.dto.ListingSearchRequestDTO;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.shared.dto.SearchResults;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TenantControllerTest")
@ExtendWith(MockitoExtension.class)
public class TenantControllerTest {

    @InjectMocks
    protected TenantController controller;

    @Mock
    protected TenantService tenantService;

    @Mock
    protected Pageable pageable;

    @Mock
    protected Page<DisplayCardListingDTO> results;

    protected DisplayCardListingDTO dto;

    @BeforeEach
    public void setup(){
        dto = TestUtil.generateDisplayCardListingDTO();
    }

    @DisplayName("Find All by Booking Category")
    @Test
    void testFindAllByBookingCategory(){
        when(tenantService.getAllByCategory(pageable, BookingCategory.ALL)).thenReturn(results);

        ResponseEntity<SearchResults<DisplayCardListingDTO>> resp = controller.findAllByBookingCategory(pageable,BookingCategory.ALL);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody().getList(),results.getContent());
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(tenantService, times(1)).getAllByCategory(pageable, BookingCategory.ALL);
    }

    @DisplayName("Search Listings")
    @Test
    void testSearchListings(){
        ListingSearchRequestDTO dto = TestUtil.generateListingSearchRequestDTO();
        when(tenantService.search(dto)).thenReturn(results);

        ResponseEntity<SearchResults<DisplayCardListingDTO>> resp =controller.searchListings(dto);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody().getList(),results.getContent());
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(tenantService, times(1)).search(dto);
    }

    @DisplayName("Get Listing")
    @Test
    void testGetDisplayListing(){
        DisplayListingDTO listing = TestUtil.generateDisplayListingDTO();
        when(tenantService.getOne(TestConstants.TEST_USER_PUBLIC_ID)).thenReturn(listing);

        ResponseEntity<DisplayListingDTO> resp = controller.getDisplayListing(TestConstants.TEST_USER_PUBLIC_ID);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(),listing);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(tenantService, times(1)).getOne(TestConstants.TEST_USER_PUBLIC_ID);
    }
}
