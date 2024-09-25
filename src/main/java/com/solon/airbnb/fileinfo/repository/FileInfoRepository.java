package com.solon.airbnb.fileinfo.repository;

import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.shared.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends BaseRepository<FileInfo, Long>,
        FileInfoCustomRepository{



}
