package com.solon.airbnb.infrastructure.config.authorization;

import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.listing.application.service.PictureService;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.service.UserService;
import com.solon.airbnb.user.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    /** Logger for the class. */
    private static final Logger LOG = LoggerFactory.getLogger(CustomMethodSecurityExpressionRoot.class);

    private Object filterObject;

    private Object returnObject;

    private Object target;

    protected User currentUser;

    protected UserService usersService;
    protected LandlordService landlordService;
    protected PictureService pictureService;
    protected TenantService tenantService;
    protected BookingService bookingService;

    public CustomMethodSecurityExpressionRoot(
            Authentication authentication,
            UserService usersService,
            LandlordService landlordService,
            PictureService pictureService,
            TenantService tenantService,
            BookingService bookingService
            ){
        super(authentication);
        this.usersService = usersService;
        this.landlordService = landlordService;
        this.pictureService = pictureService;
        this.tenantService = tenantService;
        this.bookingService = bookingService;

        UserDTO userDTO = (UserDTO) getAuthentication().getPrincipal();
        this.currentUser = this.usersService.getByPublicId(userDTO.getPublicId().toString()).orElse(null);
        this.setDefaultRolePrefix(""); // For using hasRole as: hasRole(EDConstants.ROLE_XX)
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;

    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;

    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
    }
}
