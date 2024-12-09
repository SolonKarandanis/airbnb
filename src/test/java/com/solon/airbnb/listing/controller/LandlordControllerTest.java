package com.solon.airbnb.listing.controller;

import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.exception.UserException;
import com.solon.airbnb.util.TestConstants;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("LandlordControllerTest")
@ExtendWith(MockitoExtension.class)
public class LandlordControllerTest {

    @InjectMocks
    protected LandlordController controller;

    @Mock
    protected LandlordService landlordService;

    @Mock
    protected MultipartFile image;

    @Mock
    protected Authentication authentication;

    @Mock
    protected List<DisplayCardListingDTO> list;


    protected UserDTO userDto;


    protected final Long userId = 1L;

    protected final String userPublicId = TestConstants.TEST_USER_PUBLIC_ID;

    @BeforeEach
    public void setup(){
        userDto = TestUtil.createTestUserDto(userId);
        authentication = TestUtil.getTestAuthenticationFromUserDTO(userDto);
    }

    @DisplayName("Find All Listings")
    @Test
    void testGetAllListings(){
        when(landlordService.getAllProperties(userPublicId)).thenReturn(list);

        ResponseEntity<List<DisplayCardListingDTO>> resp = controller.getAllListings(authentication);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), list);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(landlordService, times(1)).getAllProperties(userPublicId);
    }

    @DisplayName("Delete Listing")
    @Test
    void testDeleteListing(){
        String listingPublicId = TestConstants.TEST_USER_PUBLIC_ID;
        when(landlordService.delete(listingPublicId,userPublicId)).thenReturn(list);

        ResponseEntity<List<DisplayCardListingDTO>> resp =controller.deleteListing(listingPublicId,authentication);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), list);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(landlordService, times(1)).delete(listingPublicId,userPublicId);
    }

    @DisplayName("Create Listing")
    @Test
    void testCreateListing(){
        MultipartFile[] images = {image};
        SaveListingDTO saveListingDTO = TestUtil.generateSaveListingDTO();
        CreatedListingDTO newListing =TestUtil.generateCreatedListingDTO();
        List<MultipartFile> imageList  = Arrays.asList(images);
        List<PictureDTO> pictures = imageList.stream()
                .map(mapMultipartFileToPictureDTO())
                .toList();
        when(landlordService.create(userPublicId,saveListingDTO,pictures)).thenReturn(newListing);

        ResponseEntity<CreatedListingDTO> resp = controller.createListing(images,saveListingDTO,authentication);
        assertNotNull(resp);
        assertNotNull(resp.getBody());
        assertEquals(resp.getBody(), newListing);
        assertTrue(resp.getStatusCode().isSameCodeAs(HttpStatus.OK));

        verify(landlordService, times(1)).create(userPublicId,saveListingDTO,pictures);
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
