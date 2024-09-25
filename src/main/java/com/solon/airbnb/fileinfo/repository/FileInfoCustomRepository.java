package com.solon.airbnb.fileinfo.repository;

import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.shared.exception.AirbnbException;

import java.math.BigInteger;
import java.util.List;

public interface FileInfoCustomRepository {

    BigInteger generateIdFromSequencer(String sequencerName);

    FileInfo findByFileRefId(Long fid);

    void deleteByFileRefIds(List<Long> fileReferencesToDelete);

    public List<BigInteger> getFileReferenceIdsForTableColumn(String columnName, String tableName) throws AirbnbException;
}
