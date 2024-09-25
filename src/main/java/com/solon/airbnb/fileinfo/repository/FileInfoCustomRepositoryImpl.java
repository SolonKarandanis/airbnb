package com.solon.airbnb.fileinfo.repository;

import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.shared.exception.AirbnbException;

import java.math.BigInteger;
import java.util.List;

public class FileInfoCustomRepositoryImpl implements  FileInfoCustomRepository{
    
    @Override
    public BigInteger generateIdFromSequencer(String sequencerName) {
        return null;
    }

    @Override
    public FileInfo findByFileRefId(Long fid) {
        return null;
    }

    @Override
    public void deleteByFileRefIds(List<Long> fileReferencesToDelete) {

    }

    @Override
    public List<BigInteger> getFileReferenceIdsForTableColumn(String columnName, String tableName) throws AirbnbException {
        return List.of();
    }
}
