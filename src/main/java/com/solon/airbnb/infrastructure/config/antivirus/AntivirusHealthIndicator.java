package com.solon.airbnb.infrastructure.config.antivirus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.solon.airbnb.shared.service.antivirus.AntivirusService;

@Component
@ConditionalOnProperty(name = "antivirus.clamav.enabled", havingValue = "true")
public class AntivirusHealthIndicator implements HealthIndicator{

	public static final String AP_PING = "av.ping";
	
	@Autowired
    AntivirusService antivirusService;

	@Override
	public Health health() {
        final boolean result = antivirusService.ping();
        if (!result) {
            return Health.outOfService().withDetail(AP_PING, result).build();
        }
        return Health.up().build();
	}
}
