package com.solon.airbnb.fileinfo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "file_info")
public class FileInfo {

    public static final String SIGNATURE = "SIGNATURE";

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "FILIE_INFO_ID_GENERATOR", sequenceName = "file_info_generator", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILIE_INFO_ID_GENERATOR")
    private Long id = -1L;

    @Column(name = "FILE_REF_ID", nullable = false)
    private Long fileRefId;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "FILE_USAGE", nullable = false)
    private String fileUsage;

    @Column(name = "FILE_MIME_TYPE", nullable = false)
    private String mimeType;

    @Column(name = "FILE_SIZE", nullable = false)
    private Integer fileSize;

    public FileInfo() {
    }

    public FileInfo(String fileName, String mimeType, String fileUsage, Integer fileSize) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.fileUsage = fileUsage;
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName() + " Object {" + newLine);
        result.append("ID: " + this.id);
        result.append(newLine);
        result.append("}");

        return result.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUsage() {
        return fileUsage;
    }

    public void setFileUsage(String fileUsage) {
        this.fileUsage = fileUsage;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(Long fileRefId) {
        this.fileRefId = fileRefId;
    }
}
