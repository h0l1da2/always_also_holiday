package today.also.hyuil.service.fanLetter;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.BoardRemover;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.repository.fanLetter.FanLetterRepository;
import today.also.hyuil.service.admin.inter.AdminService;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.file.inter.FileService;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class FanLetterServiceImpl implements FanLetterService {

    private final FanLetterRepository fanLetterRepository;
    private final MemberJoinService memberJoinService;
    private final AdminService adminService;
    private final FileService fileService;

    public FanLetterServiceImpl(FanLetterRepository fanLetterRepository, MemberJoinService memberJoinService, AdminService adminService, FileService fileService) {
        this.fanLetterRepository = fanLetterRepository;
        this.memberJoinService = memberJoinService;
        this.adminService = adminService;
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
    public Map<String, Object> findLetter(String memberId, Long fanLetterNum) throws MemberNotFoundException {
        FanBoard fanBoard = fanLetterRepository.selectFanBoard(fanLetterNum);
        String writer = fanBoard.getMember().getMemberId();

        if (!writer.equals(memberId)) {
            throw new MemberNotFoundException("해당 멤버가 쓴 글이 아닙니다");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("fanLetter", fanBoard);

        List<FileInfo> fileInfoList = fileService.fileInfoList(fanLetterNum);

        if (fileInfoList.size() != 0) {
            map.put("fileInfoList", fileInfoList);
        }

        return map;
    }

    @Override
    public Map<String, Object> readLetter(Long num) {

        FanBoard fanBoard = fanLetterRepository.selectFanBoard(num);
        List<FileInfo> fileInfoList = fileService.fileInfoList(fanBoard.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("fanLetter", fanBoard);
        map.put("fileInfoList", fileInfoList);

        return map;
    }

    @Override
    public void modifyLetter(Map<String, Object> map) throws FileNumbersLimitExceededException {
        FanBoard fanBoard = (FanBoard) map.get("fanLetter");

        fanLetterRepository.modifyFanBoard(fanBoard);

        /**
         * 파일 찾고, 갯수가 5개 이하일 경우만
         * 맞춰서 넣을 수 있도록...
         */

        if (map.containsKey("fileInfoList")) {
            List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");
            List<FileInfo> boardFiles = fileService.fileInfoList(fanBoard.getId());

            int fileNumbers = boardFiles.size() + fileInfoList.size();

            if (5 < fileNumbers) {
                throw new FileNumbersLimitExceededException("기존 파일을 포함 5개 이상 업로드는 불가합니다");
            }

            for (FileInfo fileInfo : fileInfoList) {
                fileInfo.fanBoardFile(fanBoard);
                fileService.saveFileInfo(fileInfo);
            }
        }

        fanLetterRepository.modifyFanBoard(fanBoard);

    }

    @Override
    public void removeLetter(Long num, String who, String memberId) throws MemberNotFoundException, AccessDeniedException {

        Map<String, Object> map = readLetter(num);
        FanBoard fanBoard = (FanBoard) map.get("fanLetter");

        BoardRemover boardRemover = new BoardRemover();

        if (who.equals("MEMBER")) {
            Member member = memberJoinService.findMyAccount(memberId);

            if (member == null) {
                throw new MemberNotFoundException("해당 멤버 아이디를 찾을 수 없어요");
            }
            if (!fanBoard.getMember().getMemberId().equals(memberId)) {
                System.out.println("본인 글이 아닙니다");
                throw new AccessDeniedException("본인 글이 아닙니다");
            }
            boardRemover.memberRemove(member);
        }
        if (who.equals("ADMIN")) {
            Admin admin = adminService.accountAdmin(memberId);

            if (admin == null) {
                throw new AccessDeniedException("해당 어드민 아이디를 찾을 수 없어요");
            }
            boardRemover.adminRemove(admin);
        }

        BoardRemover remover = fanLetterRepository.insertBoardRemover(boardRemover);
        fanLetterRepository.updateLetterRemover(num, remover);
    }
}
