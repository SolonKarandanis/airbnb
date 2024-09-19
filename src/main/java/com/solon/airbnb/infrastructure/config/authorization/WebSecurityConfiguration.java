package com.solon.airbnb.infrastructure.config.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	
	@Value("${security.jwt.key}")
    private String signKey;

}
