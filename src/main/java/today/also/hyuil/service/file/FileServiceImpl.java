package today.also.hyuil.service.file;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.repository.file.FileRepository;
import today.also.hyuil.service.file.inter.FileService;

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

}
