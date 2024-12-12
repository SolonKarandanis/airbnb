package com.solon.airbnb.shared.service.antivirus;

import java.util.Collection;

import com.solon.airbnb.shared.domain.antivirus.VirusScanResult;
import com.solon.airbnb.shared.domain.antivirus.VirusScannable;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.ServiceUnavailableException;
import com.solon.airbnb.shared.exception.VirusFoundException;

public interface AntivirusService {
    /**
     * ping the anti-virus service (to be used for health-checks)
     *
     * @return
     */
    boolean ping();

    /**
     * @param scannables
     * @return
     */
    VirusScanResult scan(Collection<VirusScannable> scannables) throws VirusFoundException, AirbnbException;


    VirusScanResult scan(VirusScannable scannable) throws VirusFoundException, AirbnbException,ServiceUnavailableException;
}
