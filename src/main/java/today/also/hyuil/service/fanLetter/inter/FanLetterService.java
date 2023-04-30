package today.also.hyuil.service.fanLetter.inter;

import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface FanLetterService {

    FanBoard writeLetter(String memberId, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException;
    Map<String, Object> findLetter(String memberId, Long fanLetterNum) throws MemberNotFoundException;
    Map<String, Object> readLetter(Long num);
    void modifyLetter(Map<String, Object> map) throws FileNumbersLimitExceededException;
    void removeLetter(Long num, String who, String memberId) throws MemberNotFoundException, AccessDeniedException;
}
