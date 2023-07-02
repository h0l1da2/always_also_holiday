package today.also.hyuil.service.fanLetter;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import today.also.hyuil.domain.fanLetter.BoardRemover;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Name;
import today.also.hyuil.exception.BoardNotFoundException;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.repository.fanLetter.FanLetterJpaRepository;
import today.also.hyuil.repository.fanLetter.FanLetterRepository;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.file.inter.FileService;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FanLetterServiceImpl implements FanLetterService {

    private final FanLetterRepository fanLetterRepository;
    private final FanLetterJpaRepository fanLetterJpaRepository;
    private final MemberJoinService memberJoinService;
    private final FileService fileService;

    @Transactional
    @Override
    public FanBoard writeLetter(Long id, FanBoard fanBoard, List<FileInfo> fileInfoList) throws MemberNotFoundException {
        Member findMember = memberJoinService.findMyAccount(id);
        if (findMember == null) {
            throw new MemberNotFoundException("멤버가 없습니다");
        }
        fanBoard.writeMember(findMember);

        fanBoard = fanLetterJpaRepository.save(fanBoard);
        for (FileInfo fileInfo : fileInfoList) {
            fileInfo.fanBoardFile(fanBoard);
            fileService.saveFileInfo(fileInfo);
        }

        return fanBoard;
    }

    @Override
    public FanBoard findLetter(Long id, Long fanLetterNum) throws MemberNotFoundException, BoardNotFoundException {
        FanBoard fanBoard = fanLetterJpaRepository.findById(fanLetterNum).orElse(null);

        if (fanBoard == null) {
            throw new BoardNotFoundException("게시글을 찾을 수 없습니다");
        }
        String writer = fanBoard.getMember().getMemberId();

        if (!writer.equals(id)) {
            throw new MemberNotFoundException("해당 멤버가 쓴 글이 아닙니다");
        }

        return fanBoard;
    }

    @Override
    public Map<String, Object> readLetter(Long num) {

        FanBoard fanBoard = fanLetterRepository.selectAndViewCnt(num);
        List<FileInfo> fileInfoList = fileService.fileInfoListForFanBoard(fanBoard.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("fanLetter", fanBoard);
        map.put("fileInfoList", fileInfoList);

        return map;
    }

    @Transactional
    @Override
    public void modifyLetter(Map<String, Object> map) throws FileNumbersLimitExceededException {
        FanBoard fanBoard = (FanBoard) map.get("fanLetter");

        /**
         * 파일 찾고, 갯수가 5개 이하일 경우만
         * 맞춰서 넣을 수 있도록...
         */

        if (map.containsKey("fileInfoList")) {
            List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");
            List<FileInfo> boardFiles = fileService.fileInfoListForFanBoard(fanBoard.getId());

            int fileNumbers = boardFiles.size() + fileInfoList.size();

            if (5 < fileNumbers) {
                throw new FileNumbersLimitExceededException("기존 파일을 포함 5개 이상 업로드는 불가합니다");
            }

            for (FileInfo fileInfo : fileInfoList) {
                fileInfo.fanBoardFile(fanBoard);
                fileService.saveFileInfo(fileInfo);
            }
        }

        fanLetterJpaRepository.save(fanBoard);
    }

    @Transactional
    @Override
    public void removeLetter(Long num, Name who, Long id) throws MemberNotFoundException, AccessDeniedException {

        Map<String, Object> map = readLetter(num);
        FanBoard fanBoard = (FanBoard) map.get("fanLetter");

        BoardRemover boardRemover = new BoardRemover();

        if (who.equals(Name.ROLE_USER)) {
            Member member = memberJoinService.findMyAccount(id);

            if (member == null) {
                throw new MemberNotFoundException("해당 멤버 아이디를 찾을 수 없어요");
            }
            if (!fanBoard.getMember().getId().equals(id)) {
                throw new AccessDeniedException("본인 글이 아닙니다");
            }
            boardRemover.memberRemove(member);
        }
//        if (who.equals("ADMIN")) {
//            Admin admin = adminService.accountAdmin(id);
//
//            if (admin == null) {
//                throw new AccessDeniedException("해당 어드민 아이디를 찾을 수 없어요");
//            }
//            boardRemover.adminRemove(admin);
//        }

        BoardRemover remover = fanLetterRepository.insertBoardRemover(boardRemover);
        fanBoard.deleteFanLetter(remover);
        fanLetterJpaRepository.save(fanBoard);
    }

    @Override
    public Page<FanBoard> listMain(Pageable pageable) {

        // 현재페이지 / 페이지사이즈(10) / id 기준 오름차순 정렬
        Pageable pageRequest =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                        Sort.Direction.DESC, "id");

        return fanLetterJpaRepository.findAll(pageRequest);
    }

    @Override
    public Map<String, FanBoard> prevNextLetter(Long id) {
        Long prev = id - 1;
        Long next = id + 1;

        FanBoard prevLetter = fanLetterJpaRepository.findById(prev).orElse(null);
        FanBoard nextLetter = fanLetterJpaRepository.findById(next).orElse(null);

        Map<String, FanBoard> map = new HashMap<>();

        map.put("prev", prevLetter);
        map.put("next", nextLetter);

        return map;
    }

    @Override
    public Map<String, Object> getLetter(Long num) throws BoardNotFoundException {
        FanBoard fanBoard = fanLetterJpaRepository.findById(num).orElse(null);

        if (fanBoard == null) {
            throw new BoardNotFoundException("게시글을 찾을 수 없습니다");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("fanLetter", fanBoard);

        List<FileInfo> fileInfoList = fileService.fileInfoListForFanBoard(num);

        if (fileInfoList.size() != 0) {
            map.put("fileInfoList", fileInfoList);
        }

        return map;
    }

}