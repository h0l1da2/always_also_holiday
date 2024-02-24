package today.also.hyuil.fanletter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.fanletter.domain.FanBoard;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.member.domain.type.Name;
import today.also.hyuil.common.exception.BoardNotFoundException;
import today.also.hyuil.common.exception.FileNumbersLimitExceededException;
import today.also.hyuil.common.exception.MemberNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface FanLetterService {

    FanBoard writeLetter(Long id, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException;
    FanBoard findLetter(Long id, Long fanLetterNum) throws MemberNotFoundException, BoardNotFoundException;
    Map<String, Object> readLetter(Long num);
    void modifyLetter(Map<String, Object> map) throws FileNumbersLimitExceededException;
    void removeLetter(Long num, Name who, Long id) throws MemberNotFoundException, AccessDeniedException;
    Page<FanBoard> listMain(Pageable pageable);
    Map<String, FanBoard> prevNextLetter(Long id);
    Map<String, Object> getLetter(Long num) throws BoardNotFoundException;
}
