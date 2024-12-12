package com.solon.airbnb.shared.service.antivirus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.solon.airbnb.shared.domain.antivirus.VirusScannable;

import lombok.extern.slf4j.Slf4j;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Slf4j
@Service
@ConditionalOnProperty(name = "antivirus.clamav.enabled", havingValue = "true")
public class AntivirusServiceClamAvImpl extends AbstractAntivirusServiceImpl{
	
    @Autowired
    private ClamavClient avClient;
	
	@Override
	protected boolean doPing() {
        try {
            avClient.ping();
        } catch (Exception e) {
            log.error("error.clamav.ping", e);
            return false;
        }
        return true;
	}

	@Override
	protected Map<String, Collection<String>> doScan(VirusScannable scannable) {
		Map<String, Collection<String>> viruses = new HashMap<>();
        final ScanResult scanResult = avClient.scan(scannable.getInputStream());
        if (scanResult instanceof ScanResult.VirusFound) {
            viruses = ((ScanResult.VirusFound) scanResult).getFoundViruses();
        }
        return viruses;
	}

}
