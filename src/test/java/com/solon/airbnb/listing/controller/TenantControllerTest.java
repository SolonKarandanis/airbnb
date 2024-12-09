package com.solon.airbnb.listing.controller;


import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TenantControllerTest")
@ExtendWith(MockitoExtension.class)
public class TenantControllerTest {

    @InjectMocks
    protected TenantController controller;

    @Mock
    protected TenantService tenantService;

    protected DisplayCardListingDTO dto;

    @BeforeEach
    public void setup(){
        dto = TestUtil.generateDisplayCardListingDTO();
    }
}
