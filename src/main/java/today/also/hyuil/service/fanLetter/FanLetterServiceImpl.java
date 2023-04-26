package today.also.hyuil.service.fanLetter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.repository.fanLetter.FanLetterRepository;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.file.inter.FileService;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class FanLetterServiceImpl implements FanLetterService {

    private final FanLetterRepository fanLetterRepository;
    private final MemberJoinService memberJoinService;
    private final FileService fileService;

    public FanLetterServiceImpl(FanLetterRepository fanLetterRepository, MemberJoinService memberJoinService, FileService fileService) {
        this.fanLetterRepository = fanLetterRepository;
        this.memberJoinService = memberJoinService;
        this.fileService = fileService;
    }

    @Override
    public FanBoard writeLetter(String memberId, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException {
        Member findMember = memberJoinService.findMyAccount(memberId);
        if (findMember == null) {
            throw new MemberNotFoundException("멤버가 없습니다");
        }
        fanBoard.writeMember(findMember);

        FanBoard insertFanBoard = fanLetterRepository.insertFanBoard(fanBoard);
        for (FileInfo fileInfo : fileInfoList) {
            fileInfo.fanBoardFile(insertFanBoard);
            fileService.saveFileInfo(fileInfo);
        }

        return insertFanBoard;
    }

    @Override
    public Map<String, Object> modifyLetter(String memberId, Long fanLetterNum) throws MemberNotFoundException {
        FanBoard fanBoard = fanLetterRepository.selectFanBoard(fanLetterNum);
        String writer = fanBoard.getMember().getMemberId();

        if (!writer.equals(memberId)) {
            throw new MemberNotFoundException("해당 멤버가 쓴 글이 아닙니다");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("fanBoard", fanBoard);

        List<FileInfo> fileInfoList = fileService.fileInfoList(fanLetterNum);

        if (fileInfoList.size() != 0) {
            map.put("fileInfoList", fileInfoList);
        }

        return map;
    }
}
