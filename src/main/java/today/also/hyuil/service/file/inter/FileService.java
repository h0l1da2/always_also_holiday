package today.also.hyuil.service.file.inter;

import today.also.hyuil.domain.file.FileInfo;

import java.util.List;

public interface FileService {

    FileInfo saveFileInfo(FileInfo fileInfo);
    List<FileInfo> fileInfoListForFanBoard(Long letterNum);
    List<FileInfo> fileInfoListForMarket(Long marketId);
}
