package com.solon.airbnb.shared.service.antivirus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.solon.airbnb.shared.domain.antivirus.VirusScanResult;
import com.solon.airbnb.shared.domain.antivirus.VirusScannable;

@Service
@ConditionalOnProperty(name = "antivirus.noop.enabled", havingValue = "true")
public class AntivirusServiceNoopImpl extends AbstractAntivirusServiceImpl{

	@Override
    protected boolean doPing() {
        return true;
    }

    @Override
    protected Map<String, Collection<String>> doScan(VirusScannable scannable) {
        return new HashMap<>();
    }

    @Override
    protected VirusScanResult processScanResult(VirusScanResult scanResult) {
        return scanResult;
    }

}
