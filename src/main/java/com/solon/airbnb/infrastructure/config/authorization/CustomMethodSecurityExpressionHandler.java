package com.solon.airbnb.infrastructure.config.authorization;

import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.listing.application.service.PictureService;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.user.application.service.UserService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    protected UserService usersService;
    protected LandlordService landlordService;
    protected PictureService pictureService;
    protected TenantService tenantService;
    protected BookingService bookingService;

    public CustomMethodSecurityExpressionHandler(
            UserService usersService,
            LandlordService landlordService,
            PictureService pictureService,
            TenantService tenantService,
            BookingService bookingService) {
        this.usersService=usersService;
        this.landlordService=landlordService;
        this.pictureService=pictureService;
        this.tenantService=tenantService;
        this.bookingService=bookingService;
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root = getCustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(new AuthenticationTrustResolverImpl());
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

    protected CustomMethodSecurityExpressionRoot getCustomMethodSecurityExpressionRoot(Authentication authentication) {
        CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(
                authentication, usersService,landlordService,pictureService,tenantService,bookingService);
        return root;
    }

    @Override
    public StandardEvaluationContext createEvaluationContextInternal(final Authentication auth, final MethodInvocation mi) {
        StandardEvaluationContext standardEvaluationContext = super.createEvaluationContextInternal(auth, mi);
        ((StandardTypeLocator) standardEvaluationContext.getTypeLocator()).registerImport("com.solon.dut.service");
        return standardEvaluationContext;
    }
}
