package com.solon.airbnb.fileinfo.repository;

import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.fileinfo.domain.QFileInfo;
import com.solon.airbnb.shared.exception.AirbnbException;
import com.solon.airbnb.shared.repository.AbstractRepository;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.List;

public class FileInfoCustomRepositoryImpl extends AbstractRepository<FileInfo,Long> implements  FileInfoCustomRepository{

    private final QFileInfo fileInfo = QFileInfo.fileInfo;

    @Override
    public BigInteger generateIdFromSequencer(String sequencerName) {
        return super.generateIdFromSequencer(sequencerName);
    }

    @Override
    public FileInfo findByFileRefId(Long fid) {
        return getJpaQueryFactory()
                .select(fileInfo)
                .from(fileInfo)
                .where(fileInfo.fileRefId.eq(fid))
                .fetchOne();
    }

    @Override
    public void deleteByFileRefIds(List<Long> fileReferencesToDelete) {
        if(!CollectionUtils.isEmpty(fileReferencesToDelete)) {
            int batchSize = 1000;
            int size = fileReferencesToDelete.size();
            int times = (size / batchSize);
            times = (size % batchSize > 0) ? times + 1 : times;
            int index = 0;
            int toIndex = 0;
            for (int i = 1; i <= times; i++) {
                toIndex = (i == times) ? size : index + batchSize;
                List<Long> sublist = fileReferencesToDelete.subList(index, toIndex);
                getJpaQueryFactory()
                        .delete(fileInfo)
                        .where(fileInfo.fileRefId.in(sublist))
                        .execute();
            }
        }
    }

    /**
     * @param columnName
     *            is a Triplet with column Name, table Name and Usage in the respective positions.
     * @return Returns a list of file IDs from the query created by the triplet info.
     * @throws AirbnbException
     */
    @Override
    public List<BigInteger> getFileReferenceIdsForTableColumn(String columnName, String tableName) throws AirbnbException {
        List<BigInteger> retVal = null;

        String query = createFileIdsQuery(columnName, tableName);
        try {
            Query nsql = getEntityManager().createNativeQuery(query);
            retVal = nsql.getResultList();
        } catch (Exception e) {
            throw new AirbnbException("error.get.referenced.files.from.db.for.table.column", e);
        }
        return retVal;
    }

    /**
     * @param columnName
     * @param tableName
     * @return Returns a query for the file reference ids in the specified column and table that are not null
     */
    private String createFileIdsQuery(String columnName, String tableName) {
        String retVal;
        retVal = " SELECT o." + columnName + " as filereference " + " "
                + " FROM " + tableName + " o " + " WHERE o." + columnName + " IS NOT null ";
        return retVal;
    }
}
