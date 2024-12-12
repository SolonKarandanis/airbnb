package com.solon.airbnb.shared.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.solon.airbnb.infrastructure.config.ApplicationConfigProps;
import com.solon.airbnb.shared.domain.antivirus.ByteArrayVirusScannable;
import com.solon.airbnb.shared.domain.antivirus.InputStreamVirusScannable;
import com.solon.airbnb.shared.domain.antivirus.VirusScannable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultipartFileUtil {

    public static File convertToFile(MultipartFile multipartFile, String filePrefix) throws IOException {
        File file = new File(ApplicationConfigProps.CCM_MULTIPART_FILE_TEMP_PATH, filePrefix + multipartFile.getName());
        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);
        try (OutputStream outStream = new FileOutputStream(file)) {
            outStream.write(buffer);
        }
        return file;
    }



    public static ResponseEntity<Resource> getResourceResponse(byte[] fileData, String filename,MediaType contentType) {
        Resource resource = new ByteArrayResource(fileData);
        /* This action supports only PNG images. */
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentLength(fileData.length).contentType(contentType).body(resource);
    }
    
    
    public static VirusScannable toVirusScannable(MultipartFile multipartFile) {
        try {
            return InputStreamVirusScannable.builder().inputStream(multipartFile.getInputStream())
                    .originalFilename(multipartFile.getOriginalFilename()).size(multipartFile.getSize()).build();
        } catch (IOException e) {
            log.error("error.scan.get.stream", e);
            return ByteArrayVirusScannable.builder().originalFilename(multipartFile.getOriginalFilename())
                    .bytes(new byte[] {}).size(0).build();
        }
    }

}
