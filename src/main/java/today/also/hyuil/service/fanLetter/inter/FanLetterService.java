package today.also.hyuil.service.fanLetter.inter;

import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.exception.MemberNotFoundException;

import java.util.List;
import java.util.Map;

public interface FanLetterService {

    FanBoard writeLetter(String memberId, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException;
    public Map<String, Object> modifyLetter(String memberId, Long fanLetterNum) throws MemberNotFoundException;
}
