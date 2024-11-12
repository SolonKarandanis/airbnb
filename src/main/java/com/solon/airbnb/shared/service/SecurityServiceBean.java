package com.solon.airbnb.shared.service;

import com.solon.airbnb.listing.application.service.LandlordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SecurityServiceBean extends GenericServiceBean implements SecurityService{

    private final LandlordService landlordService;

    public SecurityServiceBean(LandlordService landlordService) {
        this.landlordService = landlordService;
    }

    @Override
    public boolean isListingMine(String listingPublicId) {
        return false;
    }
}
