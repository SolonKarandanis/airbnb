package com.solon.airbnb.shared.domain.antivirus;

import java.io.InputStream;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InputStreamVirusScannable implements VirusScannable{

    private InputStream inputStream;
    private String originalFilename;
    private long size;
}
