package com.solon.airbnb.fileinfo.application.service;

import com.solon.airbnb.fileinfo.application.dto.AirbnbFileDTO;
import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.fileinfo.repository.FileInfoRepository;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service("fileService")
@Transactional
public class FileServiceBean extends GenericServiceBean implements FileService{

    private static final Logger log = LoggerFactory.getLogger(FileServiceBean.class);

    @Value("${file.storage.path}")
    private static String fileStoragePath;

    private static File TEMP_DIR = new File(fileStoragePath, "__tmp__");

    static {
        TEMP_DIR.mkdirs();
    }

    private final FileInfoRepository fileInfoRepoisotory;

    public FileServiceBean(FileInfoRepository fileInfoRepoisotory) {
        this.fileInfoRepoisotory= fileInfoRepoisotory;
    }

    /* **********************A T T E N T I O N********** I M P O R T A N T ****************************
     * If you have introduced a new file ( new column ) it should be referenced in "fileUsages" list  *
     * in "FileInfo" class. Files in columns not referenced in fileUsages list will be deleted during *
     * file system clean up daily process.                                                            *
     * ************************************************************************************************
     */
    @Override
    public BigInteger createFileInfo(FileInfo fileInfo) throws AirbnbException {
        try {
            if (fileInfo.getFileRefId() == null) {
                BigInteger fid = fileInfoRepoisotory.generateIdFromSequencer("file_info_generator");
                fileInfo.setFileRefId(fid.longValue());
            }
            fileInfoRepoisotory.save(fileInfo);
            return BigInteger.valueOf(fileInfo.getFileRefId());
        }
        catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AirbnbException("error.create.file", ex);
        }
    }


    /* **********************A T T E N T I O N********** I M P O R T A N T ****************************
     * If you have introduced a new file ( new column ) it should be referenced in "fileUsages" list  *
     * in "FileInfo" class. Files in columns not referenced in fileUsages list will be deleted during *
     * file system clean up daily process.                                                            *
     * ************************************************************************************************
     *
     * NOTE! Returns the fileReferenceId (which maps to the file system) not the FileInfo id
     * (non-Javadoc)
     * @see com.ed.ccm.service.FileService#createFile(byte[], com.ed.ccm.domain.FileInfo)
     */
    @Override
    public BigInteger createFile(byte[] content, FileInfo fileInfo) throws AirbnbException {
        return null;
    }

    @Override
    public int createFiles(Map<FileInfo, byte[]> dataMap) throws AirbnbException {
        return 0;
    }

    @Override
    public boolean fileExistsInStorage(BigInteger fid) {
        return false;
    }

    @Override
    public byte[] getFileContentById(BigInteger fid) throws AirbnbException {
        return new byte[0];
    }

    @Override
    public boolean deleteFile(BigInteger fid) {
        return false;
    }

    @Override
    public boolean deleteFileInfo(BigInteger fid) {
        return false;
    }

    @Override
    public boolean deleteOnFileSystem(BigInteger fid) {
        return false;
    }

    @Override
    public Map<String, FileInfo> unzipFile(BigInteger fid) throws AirbnbException {
        return Map.of();
    }



    @Override
    public Map<String, AirbnbFileDTO> getAllFilesInFileSystem(String fileSystemPath) {
        return Map.of();
    }

    @Override
    public String getFileId(BigInteger fid) {
        return "";
    }

    @Override
    public Map<String, AirbnbFileDTO> crawlDirectoryForFiles(String path, String pathId, Map<String, AirbnbFileDTO> files) {
        return Map.of();
    }

    @Override
    public void deleteRedundantFiles(Map<String, AirbnbFileDTO> ccmFilesToDelete) {

    }

    @Override
    public void filesystemCleanUp() {

    }

    @Override
    public void scheduleStartCronJobFilesystemCleanUp() throws Exception {

    }

    @Override
    public void scheduleDeleteCronJobFilesystemCleanUp() throws Exception {

    }

    @Override
    public void deleteFiles(List<BigInteger> fileReferencesToDelete) throws Exception {

    }

    @Override
    public FileInfo getFileInfoByFileReferenceId(Long fileReferenceId) {
        return null;
    }
}
