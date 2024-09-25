package com.solon.airbnb.fileinfo.application.dto;

public class AirbnbFileDTO {
    private String fileId;
    private String fullPath;
    private Long dateLastModified;
    private Long fileSize;

    public AirbnbFileDTO(String fileId, String fullPath, Long dateLastModified, Long fileSize) {
        this.fileId = fileId;
        this.fullPath = fullPath;
        this.dateLastModified = dateLastModified;
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        String retVal = fileId + "," + fullPath + "," + dateLastModified + "," + fileSize;
        return retVal;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Long dateLastModified) {
        this.dateLastModified = dateLastModified;
    }
}
