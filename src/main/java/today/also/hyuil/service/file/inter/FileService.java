package today.also.hyuil.service.file.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;

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
