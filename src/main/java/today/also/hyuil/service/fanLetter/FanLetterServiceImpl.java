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

import java.util.List;

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
}
