package com.solon.airbnb.shared.domain.antivirus;

import java.io.Serializable;
import java.util.List;

import lombok.Singular;

public class VirusScanResult implements Serializable{

	private static final long serialVersionUID = -3985600099243208925L;

	private Result result;


    @Singular
    private List<VirusScanResultFileList> infectedFiles;

    @Singular
    private List<VirusScanResultFileList> cleanFiles;

    @Singular
    private List<VirusScanResultFileList> errorFiles;
}
