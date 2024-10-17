package com.solon.airbnb.listing.controller;

import com.solon.airbnb.shared.controller.GenericController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/landlord")
public class LandlordController extends GenericController {

    private static final Logger log = LoggerFactory.getLogger(LandlordController.class);


}
