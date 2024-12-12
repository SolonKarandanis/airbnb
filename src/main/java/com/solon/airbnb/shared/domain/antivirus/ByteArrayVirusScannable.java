package com.solon.airbnb.shared.domain.antivirus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ByteArrayVirusScannable implements VirusScannable{
	
    private byte[] bytes;
    private String originalFilename;
    private long size;

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(bytes);
	}

	

}
