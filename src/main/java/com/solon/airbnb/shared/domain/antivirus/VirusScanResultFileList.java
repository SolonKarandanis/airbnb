package com.solon.airbnb.shared.domain.antivirus;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VirusScanResultFileList implements Serializable{

	private static final long serialVersionUID = -4485025196343159623L;
	
	private String fileName;

    private Result result;

    @Singular
    private Collection<VirusScanResultFile> scanFiles;

}
