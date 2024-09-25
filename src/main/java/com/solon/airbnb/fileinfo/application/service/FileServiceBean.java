package com.solon.airbnb.fileinfo.application.service;

import com.solon.airbnb.fileinfo.application.dto.AirbnbFileDTO;
import com.solon.airbnb.fileinfo.constants.FileConstants;
import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.fileinfo.repository.FileInfoRepository;
import com.solon.airbnb.fileinfo.util.FileUtil;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.service.GenericServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

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
        try {
            createFileInfo(fileInfo);
            storeFile(fileInfo, content);
            return BigInteger.valueOf(fileInfo.getFileRefId());
        } catch (Exception ex) {
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
     * (non-Javadoc)
     *
     * @see com.ed.ccm.service.FileService#storeFiles(java.util.Map)
     */
    @Override
    public int createFiles(Map<FileInfo, byte[]> dataMap) throws AirbnbException {
        int res = 0;
        for (Map.Entry<FileInfo, byte[]> dataMapEntry : dataMap.entrySet()) {
            byte[] f = dataMapEntry.getValue();
            createFile(f, dataMapEntry.getKey());
            res++;
        }
        return res;
    }

    @Override
    public boolean fileExistsInStorage(BigInteger fid) {
        String[] paths = convertIdToPath(fid);
        File res = new File(paths[0], paths[1]);
        return res.isFile();
    }

    @Override
    public byte[] getFileContentById(BigInteger fid) throws AirbnbException {
        String[] paths = convertIdToPath(fid);
        File res = new File(paths[0], paths[1]);
        try {
            return FileUtil.getBytesFromFile(res);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new AirbnbException("error.reading.file", ex);
        }
    }

    @Override
    public boolean deleteFile(BigInteger fid) {
        FileInfo fileInfo = fileInfoRepoisotory.findByFileRefId(fid.longValue());
        if (fileInfo != null) { //if not its an old file reference without a file info entry
            fileInfoRepoisotory.delete(fileInfo);
        }
        String[] paths = convertIdToPath(fid);
        File res = new File(paths[0], paths[1]);
        return res.delete();
    }

    @Override
    public boolean deleteFileInfo(BigInteger fid) {
        boolean isDeleted = false;
        FileInfo fileInfo = fileInfoRepoisotory.findByFileRefId(fid.longValue());
        if (fileInfo != null) { //if not its an old file reference without a file info entry
            fileInfoRepoisotory.delete(fileInfo);
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public boolean deleteOnFileSystem(BigInteger fid) {
        String[] paths = convertIdToPath(fid);
        File res = new File(paths[0], paths[1]);
        return res.delete();
    }

    @Override
    public void deleteFiles(List<BigInteger> fileReferencesToDelete) throws Exception {
        List<Long> refs = new ArrayList<>();
        for (BigInteger fid : fileReferencesToDelete) {
            refs.add(Long.valueOf(fid.longValue()));
        }
        fileInfoRepoisotory.deleteByFileRefIds(refs);
        for (BigInteger fid : fileReferencesToDelete) {
            String[] paths = convertIdToPath(fid);
            File res = new File(paths[0], paths[1]);
            res.delete();
        }
    }

    @Override
    public Map<String, FileInfo> unzipFile(BigInteger fid) throws AirbnbException {
        return Map.of();
    }



    /**
     * @param fileSystemPath
     *            The path that should be checked for files
     * @return Returns all files included in the specified path and subdirectories, excluding directories specified in
     *         constants
     */
    @Override
    public Map<String, AirbnbFileDTO> getAllFilesInFileSystem(String fileSystemPath) {
        Map<String, AirbnbFileDTO> retVal = new HashMap<>();
        File fl = new File(fileSystemPath);
        if (fl.exists() && fl.isDirectory()) {
            File[] subFiles = fl.listFiles();
            for(int i = 0; i < subFiles.length; i++) {
                File subFile = subFiles[i];
                //if File is a directory and it's name starts with a Digit ( dut files directories start with digit )
                if ((subFile.isDirectory()) && (Character.isDigit(subFile.getName().charAt(0)))) {
                    log.trace("Crawl for files in directory: {}" , subFile.getAbsolutePath());
                    retVal.putAll(crawlDirectoryForFiles(subFile.getAbsolutePath(), "", retVal));
                }
            }
        }
        return retVal;
    }

    @Override
    public String getFileId(BigInteger fid) {
        String fileId = String.format("%012d", fid);
        return fileId;
    }

    /**
     * @param path
     *            The path to start crawling
     * @param pathId
     *            The id of the file until this path
     * @param files
     *            The map of files.
     * @return Returns all the files under the initial path
     */
    @Override
    public Map<String, AirbnbFileDTO> crawlDirectoryForFiles(String path, String pathId, Map<String, AirbnbFileDTO> files) {
        File fl = new File(path);
        if (fl.isDirectory()) {
            File[] subFiles = fl.listFiles();
            if (subFiles.length > 0) {
                for (int i = 0; i < subFiles.length; i++) {
                    File subFile = subFiles[i];
                    String newPath = addToPath(path, subFile.getName());
                    String newPathId = pathId + fl.getName();
                    files = crawlDirectoryForFiles(newPath, newPathId, files);
                }
            }
        }
        else if (fl.isFile()) {
            String fileId = pathId + fl.getName();
            String fullPath = fl.getAbsolutePath();
            Long dateLastModified = fl.lastModified();
            Long fileSize = fl.length();
            AirbnbFileDTO ccmFile = new AirbnbFileDTO(fileId, fullPath, dateLastModified, fileSize);
            files.put(fileId, ccmFile);
        } else {
            log.info("Unknown type");
        }
        return files;
    }


    /**
     * Deletes all redundant files from the filesystem.
     *
     * @param ccmFilesToDelete
     *            Map that contains the redundant files in the filesystem
     */
    @Override
    public void deleteRedundantFiles(Map<String, AirbnbFileDTO> ccmFilesToDelete) {
        log.info("---Started Delete Redundant Files");
        for (Map.Entry<String, AirbnbFileDTO> entry : ccmFilesToDelete.entrySet()) {
            AirbnbFileDTO fileToDelete = entry.getValue();
            //Convert File id to BigInteger
            Long fileId = Long.valueOf(fileToDelete.getFileId());
            BigInteger fid = BigInteger.valueOf(fileId);
            deleteFile(fid);
        }
        log.info("---Finished Delete Redundant files");
    }

    /**
     * This method finds which dut files in file storage are not referenced in the db and deletes them.
     */
    @Override
    public void filesystemCleanUp() {
        log.info("Started file system clean up");
        log.info("FILE SYSTEM CLEAN UP - QUERIES COUNT: {}", FileConstants.fileUsages.size());
        //Get all file IDs referenced in the db. The list of the columns that contain File IDs is
        List<BigInteger> filesInDB = new ArrayList<>();
        try {
            for(Map.Entry<String, String> entry: FileConstants.fileUsages.entrySet()) {
                String tableName = entry.getKey();
                String columnName = entry.getValue();
                List<BigInteger> filesList = fileInfoRepoisotory.getFileReferenceIdsForTableColumn(columnName,tableName);
                filesInDB.addAll(filesList);
            }
            //Make the ids in the list unique
            Set<BigInteger> set = new HashSet<>(filesInDB);
            filesInDB = new ArrayList<>(set);
            log.debug("FILE SYSTEM CLEAN UP - FILES REFERENCED IN DB COUNT: {}",filesInDB.size());
            //get all dut files from file storage
            Map<String, AirbnbFileDTO> filesInFileSystem = getAllFilesInFileSystem(fileStoragePath);
            if(!CollectionUtils.isEmpty(filesInFileSystem)) {
                //Find which files are redundant
                Map<String, AirbnbFileDTO> redundantFiles = new HashMap<>();
                redundantFiles.putAll(filesInFileSystem);
                for (BigInteger fileID : filesInDB) {
                    //Get the 12 Characters file ID String
                    String fileId = getFileId(fileID);
                    redundantFiles.remove(fileId);
                }
                deleteRedundantFiles(redundantFiles);
            }
            else {
                log.debug("No files found referenced in the db. Clean up ABORTED.");
                throw new AirbnbException("no.files.referenced.in.db");
            }
            log.debug("Finished file system clean up");
        }
        catch (Exception e) {
            log.error("File System Clean up FAILED.", e);
        }
    }

    @Override
    public void scheduleStartCronJobFilesystemCleanUp() throws Exception {
//        int hour = DutConstants.START_FILESYSTEM_CLEANUP_HOUR;
//        int min = DutConstants.START_FILESYSTEM_CLEANUP_MIN;
//        Jobkey key = new Jobkey(new String(DutConstants.SCHEDULER_JOB_FILE_SYSTEM_CLEAN_UP), DutConstants.SCHEDULER_TOP_MODULE_NAME_ADMIN,
//                DutConstants.SCHEDULER_MODULE_FILESYSTEM, DutConstants.SCHEDULER_CRON_JOB_FILESYSTEM);
//        String jndiName = DutConstants.FILE_SERVICE_NAME;
//        JobDetail jobDetail = schedulingService.createJobDetail(key, null, jndiName, "filesystemCleanUp", null);
//        schedulingService.scheduleDailyCronJobAtTime(key, jobDetail, hour, min);
    }

    @Override
    public void scheduleDeleteCronJobFilesystemCleanUp() throws Exception {
//        log.debug("delete existing filesystem cleanup job");
//        Jobkey key = new Jobkey(new String(DutConstants.SCHEDULER_JOB_FILE_SYSTEM_CLEAN_UP), DutConstants.SCHEDULER_TOP_MODULE_NAME_ADMIN,
//                DutConstants.SCHEDULER_MODULE_FILESYSTEM, DutConstants.SCHEDULER_CRON_JOB_FILESYSTEM);
//        String jndiName = DutConstants.FILE_SERVICE_NAME;
//        JobDetail jobDetail = schedulingService.createJobDetail(key, null, jndiName, "filesystemCleanUp", null);
//        if (jobDetail != null) {
//            schedulingService.deleteTriggerFromScheduler(key, jobDetail.getKey().getGroup());
//            schedulingService.deleteJobFromScheduler(key, jobDetail.getKey().getGroup());
//        }
    }



    @Override
    public FileInfo getFileInfoByFileReferenceId(Long fileReferenceId) {
        return fileInfoRepoisotory.findByFileRefId(fileReferenceId);
    }

    /* **********************A T T E N T I O N********** I M P O R T A N T ****************************
     * If you have introduced a new file ( new column ) it should be referenced in "fileUsages" list  *
     * in "FileInfo" class. Files in columns not referenced in fileUsages list will be deleted during *
     * file system clean up daily process.                                                            *
     * ************************************************************************************************
     *
     * (non-Javadoc)
     *
     * @see com.ed.ccm.service.FileService#storeFile(java.lang.BigInteger,
     * byte[])
     */
    private int storeFile(FileInfo fileInfo, byte[] content) throws AirbnbException{
        BigInteger fid = null;
        if (fileInfo.getId() <= 0) {
            createFileInfo(fileInfo).longValue();
        }
        fid = BigInteger.valueOf(fileInfo.getFileRefId());
        String[] paths = convertIdToPath(BigInteger.valueOf(fileInfo.getFileRefId()));
        File dir = new File(paths[0]);
        dir.mkdirs();
        // First we write to temp file and at the succesful end we rename to the target file.
        // So, we will not corrupt any existing file, or even have any corrupted new files
        File outFile = new File(dir, paths[1]);
        File tempFile = new File(TEMP_DIR, fid.toString());
        try {
            FileUtil.writeBytesToFile(tempFile, content);
            boolean success;
            outFile.delete();
            success = tempFile.renameTo(outFile);
            if (!success) {
                throw new IOException("Could not rename file: " + fid);
            }
        }
        catch (Exception ex) {
            log.error(ex.getMessage());
            throw new AirbnbException("error.writing.file", ex);
        }
        return 1;
    }

    /**
     * Helper method for id to path transformation.
     *
     * @param fid
     *            Unique file identifier.
     * @return Relative path as array containing two elements:{absolute path,
     *         file name}
     */
    private String[] convertIdToPath(BigInteger fid) {
        String[] res = new String[2];
        String fileId = String.format("%012d", fid);
        StringBuilder sb = new StringBuilder(fileStoragePath); //StringBuilder will allocate input.length + 16 bytes
        sb.append("/").append(fileId.substring(0, 3));
        sb.append("/").append(fileId.substring(3, 6));
        sb.append("/").append(fileId.substring(6, 9));
        res[0] = sb.toString();
        res[1] = fileId.substring(9);
        return res;

    }

    /**
     * @param path
     *            Path to which the input will be added
     * @param input
     *            the string that should be added to the path
     * @return Returns a final path with concatenated the path and the input
     */
    private String addToPath(String path, String input) {
        String retVal = "";
        if (path.endsWith(File.separator)) {
            retVal = path + input;
        } else {
            retVal = path + File.separator + input;
        }
        return retVal;

    }
}
