package com.solon.airbnb.infrastructure.config.antivirus;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class AntivirusHealthIndicator implements HealthIndicator{

	public static final String AP_PING = "av.ping";

	@Override
	public Health health() {
		// TODO Auto-generated method stub
		return null;
	}
}
