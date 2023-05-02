package today.also.hyuil.service.fanLetter.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.domain.dto.fanLetter.FanLetterListDto;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface FanLetterService {

    FanBoard writeLetter(Long id, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException;
    Map<String, Object> findLetter(Long id, Long fanLetterNum) throws MemberNotFoundException;
    Map<String, Object> readLetter(Long num);
    void modifyLetter(Map<String, Object> map) throws FileNumbersLimitExceededException;
    void removeLetter(Long num, String who, Long id) throws MemberNotFoundException, AccessDeniedException;
    Page<FanLetterListDto> listMain(Pageable pageable);
}
