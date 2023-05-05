package today.also.hyuil.service.file;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.repository.file.FileRepository;
import today.also.hyuil.service.file.inter.FileService;

import java.util.List;

@Transactional
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileInfo saveFileInfo(FileInfo fileInfo) {
        return fileRepository.insertFileInfo(fileInfo);
    }

    @Override
    public List<FileInfo> fileInfoListForFanBoard(Long letterNum) {
        return fileRepository.selectInfoListForFanBoard(letterNum);
    }

    @Override
    public List<FileInfo> fileInfoListForMarket(Long marketId) {
        return fileRepository.selectInfoListForMarket(marketId);
    }

}
