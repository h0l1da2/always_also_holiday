package today.also.hyuil.service.file;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.repository.file.FileJpaRepository;
import today.also.hyuil.repository.file.FileRepository;
import today.also.hyuil.service.file.inter.FileService;

import java.util.List;

@Transactional
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileJpaRepository fileJpaRepository;

    public FileServiceImpl(FileRepository fileRepository, FileJpaRepository fileJpaRepository) {
        this.fileRepository = fileRepository;
        this.fileJpaRepository = fileJpaRepository;
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

    @Override
    public Page<FileInfo> fileInfoListForMarketSellList(Pageable pageable) {
        Pageable pageRequest =
                PageRequest.of(pageable.getPageNumber(), 6, Sort.Direction.DESC, "id");

        return fileJpaRepository.findAll(pageRequest);
    }

}
