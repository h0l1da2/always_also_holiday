package today.also.hyuil.file.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.common.exception.fanLetter.MimeTypeNotMatchException;

import java.io.IOException;
import java.util.List;

public interface FileService {

    FileInfo saveFileInfo(FileInfo fileInfo);
    List<FileInfo> fileInfoListForFanBoard(Long letterNum);
    List<FileInfo> fileInfoListForMarket(Long marketId);
    Page<FileInfo> fileInfoListForMarketSellList(Pageable pageable);
    List<String> getFilePaths(List<FileInfo> fileInfoList);
    List<FileInfo> getFileInfoList(String dir, List<MultipartFile> files) throws IOException, MimeTypeNotMatchException;

}
