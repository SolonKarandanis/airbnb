package com.solon.airbnb.infrastructure.config.authorization;

import com.solon.airbnb.booking.application.service.BookingService;
import com.solon.airbnb.listing.application.service.LandlordService;
import com.solon.airbnb.listing.application.service.PictureService;
import com.solon.airbnb.listing.application.service.TenantService;
import com.solon.airbnb.user.application.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {

    private ApplicationContext applicationContext;
    protected UserService usersService;
    protected LandlordService landlordService;
    protected PictureService pictureService;
    protected TenantService tenantService;
    protected BookingService bookingService;


    public MethodSecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext=applicationContext;
    }

    @Bean
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler(
                usersService,landlordService,pictureService,tenantService,bookingService);
//        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        expressionHandler.setApplicationContext(applicationContext);
        setCustomMethodSecurityExpressionHandler(expressionHandler);
        return expressionHandler;
    }

    private CustomMethodSecurityExpressionHandler methodSecurityExpressionHandler;

    public CustomMethodSecurityExpressionHandler getMethodSecurityExpressionHandler() {
        return  this.methodSecurityExpressionHandler;
    }

    private void setCustomMethodSecurityExpressionHandler(
            CustomMethodSecurityExpressionHandler expressionHandler) {
        this.methodSecurityExpressionHandler = expressionHandler;
    }
}
