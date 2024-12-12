package com.solon.airbnb.shared.service.antivirus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.solon.airbnb.shared.domain.antivirus.Result;
import com.solon.airbnb.shared.domain.antivirus.VirusScanResult;
import com.solon.airbnb.shared.domain.antivirus.VirusScanResult.VirusScanResultBuilder;
import com.solon.airbnb.shared.domain.antivirus.VirusScanResultFile;
import com.solon.airbnb.shared.domain.antivirus.VirusScanResultFileList;
import com.solon.airbnb.shared.domain.antivirus.VirusScanResultFileList.VirusScanResultFileListBuilder;
import com.solon.airbnb.shared.domain.antivirus.VirusScannable;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.exception.ServiceUnavailableException;
import com.solon.airbnb.shared.exception.VirusFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAntivirusServiceImpl implements AntivirusService {

	@Override
	public boolean ping() {
		try {
            return doPing();
        } catch (Exception e) {
            log.error("error.ping", e);
            return false;
        }
	}
	
    protected void checkIfServiceAvailable() throws ServiceUnavailableException {
        boolean check = ping();
        if (!check) {
            throw new ServiceUnavailableException("error.antivirus.service.unavailable");
        }
    }

	@Override
	public VirusScanResult scan(Collection<VirusScannable> scannables) throws VirusFoundException, AirbnbException {
        VirusScanResultBuilder scanResultBuilder = VirusScanResult.builder();
        boolean virusFound = false;
        try {
            for (VirusScannable scannable : scannables) {
                final VirusScanResult tmptVirusScanResult = scan(scannable);
                virusFound = Result.VIRUS_FOUND.equals(tmptVirusScanResult.getResult());
                tmptVirusScanResult.getCleanFiles().forEach(c -> {
                    scanResultBuilder.cleanFile(c);
                });
                tmptVirusScanResult.getInfectedFiles().forEach(i -> {
                    scanResultBuilder.infectedFile(i);
                });
                tmptVirusScanResult.getErrorFiles().forEach(e -> {
                    scanResultBuilder.errorFile(e);
                });
            }
            scanResultBuilder.result(virusFound ? Result.VIRUS_FOUND : Result.OK);
        } catch (Exception e) {
            scanResultBuilder.result(Result.ERROR);
            log.error("error.antivirus.scan", e);
        }
        VirusScanResult scanResult = scanResultBuilder.build();
        scanResult = processScanResult(scanResult);
        return scanResult;
	}

	@Override
	public VirusScanResult scan(VirusScannable scannable) throws VirusFoundException, AirbnbException, ServiceUnavailableException{
		 VirusScanResultBuilder scanResultBuilder = VirusScanResult.builder();
         VirusScanResultFileListBuilder scanFileListBuilder = VirusScanResultFileList.builder()
                .fileName(scannable.getOriginalFilename());
         
         if (scannable.getSize() <= 0) {
             log.debug("skipping file: {} of size: {}", scannable.getOriginalFilename(), scannable.getSize());
             scanFileListBuilder.result(Result.ERROR);
             scanResultBuilder.errorFile(scanFileListBuilder.build());
             return scanResultBuilder.build();
         }
         log.debug("scanning file: {} of size: {}", scannable.getOriginalFilename(), scannable.getSize());
         checkIfServiceAvailable();
         Map<String, Collection<String>> viruses = doScan(scannable);
         if (viruses.isEmpty()) {
             scanFileListBuilder.result(Result.OK);
             scanResultBuilder.cleanFile(scanFileListBuilder.build());

         } else {
             scanFileListBuilder.fileName(scannable.getOriginalFilename());
             scanFileListBuilder.result(Result.VIRUS_FOUND);
             log.debug("scan result for file: {} viruses: {}", scannable.getOriginalFilename(), viruses);
             for (Map.Entry<String, Collection<String>> virusEntrySet : viruses.entrySet()) {
                 scanFileListBuilder.scanFile(VirusScanResultFile.builder().fileAlias(virusEntrySet.getKey())
                         .viruses(virusEntrySet.getValue()).build());
             }
             scanResultBuilder.result(Result.VIRUS_FOUND);
             scanResultBuilder.infectedFile(scanFileListBuilder.build());
         }
         VirusScanResult scanResult = scanResultBuilder.build();
         scanResult = processScanResult(scanResult);
         return scanResult;
	}
	
	protected abstract boolean doPing();
    protected abstract Map<String, Collection<String>> doScan(VirusScannable scannable);
    
    protected VirusScanResult processScanResult(VirusScanResult scanResult) throws VirusFoundException, AirbnbException {
        List<VirusScanResultFileList> infectedFiles = scanResult.getInfectedFiles();
        if (infectedFiles != null && !infectedFiles.isEmpty()) {
            throw new VirusFoundException("error.antivirus.scan.VIRUS_FOUND");
        }
        List<VirusScanResultFileList> errorFiles = scanResult.getErrorFiles();
        if (errorFiles != null && !errorFiles.isEmpty()) {
            throw new AirbnbException("error.antivirus.scan.ERROR");
        }
        return scanResult;
    }

}
