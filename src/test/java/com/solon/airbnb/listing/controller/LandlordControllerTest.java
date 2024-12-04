package com.solon.airbnb.listing.controller;

import com.solon.airbnb.listing.application.service.LandlordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("LandlordControllerTest")
@ExtendWith(MockitoExtension.class)
public class LandlordControllerTest {

    @InjectMocks
    protected LandlordController controller;

    @Mock
    protected LandlordService landlordService;

    @Mock
    protected MultipartFile[] images;

    @BeforeEach
    public void setup(){

    }
}
