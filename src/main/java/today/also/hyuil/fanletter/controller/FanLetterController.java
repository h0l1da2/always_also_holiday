package today.also.hyuil.fanletter.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.fanletter.domain.Comment;
import today.also.hyuil.fanletter.domain.FanBoard;
import today.also.hyuil.fanletter.dto.*;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.file.dto.FileDto;
import today.also.hyuil.member.domain.type.Name;
import today.also.hyuil.common.exception.BoardNotFoundException;
import today.also.hyuil.common.exception.FileNumbersLimitExceededException;
import today.also.hyuil.common.exception.MemberNotFoundException;
import today.also.hyuil.common.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.fanletter.service.FanLetterCommentService;
import today.also.hyuil.fanletter.service.FanLetterService;
import today.also.hyuil.file.service.FileService;
import today.also.hyuil.common.web.WebService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fanLetter")
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@Slf4j
public class FanLetterController {

    private final WebService webService;
    private final FanLetterService fanLetterService;
    private final FanLetterCommentService fanLetterCommentService;
    private final FileService fileService;

    @GetMapping
    public String fanLetterList(@PageableDefault Pageable pageable, Model model) {
        Page<FanBoard> fanBoards = fanLetterService.listMain(pageable);
        Page<BoardListDto> fanLetterList = fanBoards
                .map(fanboard -> new BoardListDto(fanboard));
        model.addAttribute("fanLetterList", fanLetterList);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "fanLetter/boardList";
    }

    @GetMapping("/{num}")
    public String fanLetter(@PathVariable Long num, Model model, HttpServletRequest request) {
        // 글
        Map<String, Object> map = fanLetterService.readLetter(num);

        FanBoard fanBoard = (FanBoard) map.get("fanLetter");
        List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");

        List<String> filePaths = fileService.getFilePaths(fileInfoList);

        // 댓글
        List<Comment> commentList = fanLetterCommentService.readComment(num);
        List<CommentDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {

            CommentDto commentDto = new CommentDto(comment);

            if (comment.getCommentRemover() != null) {
                commentDto.itRemoved();
            }
            comments.add(commentDto);
        }

        // 이전글 다음글
        Map<String, FanBoard> prevNextLetter = fanLetterService.prevNextLetter(num);

        FanBoard prevLetter = prevNextLetter.get("prev");
        FanBoard nextLetter = prevNextLetter.get("next");

        if (prevLetter != null) {
            model.addAttribute("prev", new PrevNextDto(prevLetter.getId(), prevLetter.getTitle()));
        }
        if (nextLetter != null) {
            model.addAttribute("next", new PrevNextDto(nextLetter.getId(), nextLetter.getTitle()));
        }


        model.addAttribute("fanLetter", new FanLetterViewDto(fanBoard, comments.size()*1L));
        model.addAttribute("filePath", filePaths);
        model.addAttribute("comments", comments);

        // 본인 글인지 확인
        try {
            Long memberId = webService.getIdInSession(request);
            if (memberId.equals(fanBoard.getMember().getId())) {
                model.addAttribute("writer", memberId);
            }
        } catch (MemberNotFoundException e) {
            log.info("로그인이 안 되어있음");
        }

        return "/fanLetter/viewPage";
    }



    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("fanLetter", new FanLetterWriteDto());
        return "fanLetter/writePage";
    }

    @ResponseBody
    @PostMapping(value = "/write", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> write(@RequestPart(value = "image", required = false) List<MultipartFile> files,
                                        @RequestPart(value = "fanLetterWriteDto") FanLetterWriteDto fanLetterWriteDto,
                                        HttpServletRequest request) {

        try {
            // 세션에서 memberId 가져오기
            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            FanBoard fanBoard = new FanBoard(fanLetterWriteDto);

            // 이미지 파일이 존재할 경우
            List<FileInfo> fileInfoList = fileService.getFileInfoList("fanLetter", files);

            FanBoard writeLetter = fanLetterService.writeLetter(id, fanBoard, fileInfoList);

            if (writeLetter.getId() == null) {
                log.info("작성 오류");
                return new ResponseEntity<>("WRITE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (MimeTypeNotMatchException e) {
            e.printStackTrace();
            log.info("이미지 파일 확장자 다름");
            return new ResponseEntity<>("MIMETYPE_ERROR", HttpStatus.BAD_REQUEST);
        } catch (MemberNotFoundException me) {
            me.printStackTrace();
            log.info("로그인이 안 됨");
            return new ResponseEntity<>("NOT_LOGIN", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("파일 업로드 에러");
            e.printStackTrace();
            return new ResponseEntity<>("FILE_UPLOAD_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("WRITE_OK", HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/modify/{num}")
    public ResponseEntity<String> modifyRequest(@PathVariable Long num, HttpServletRequest request) {
        try {
            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            fanLetterService.findLetter(id, num);

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (BoardNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BOARD_NOT_FOUND");
        }
        return okResponseEntity();
    }

    @GetMapping("/modify/{num}")
    public String modify(@PathVariable Long num, Model model) {
        try {
            Map<String, Object> map = fanLetterService.getLetter(num);
            modelInFanBoard(model, map);

            if (map.containsKey("fileInfoList")) {
                modelInFileInfoList(model, map);
            }

        } catch (BoardNotFoundException e) {
            log.info("{} 번 게시글을 찾을 수 없습니다.", num);
            return "redirect:/fanLetter";
        }
        return "fanLetter/modifyPage";
    }

    @ResponseBody
    @PatchMapping(value = "/modify/{num}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> modify(@PathVariable Long num, HttpServletRequest request,
                                         @RequestPart(value = "image", required = false) List<MultipartFile> files,
                                         @RequestPart(value = "fanLetterWriteDto") FanLetterWriteDto fanLetterWriteDto) {
        try {
            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            Map<String, Object> map = fanLetterService.readLetter(num);

            FanBoard findLetter = (FanBoard) map.get("fanLetter");

            if (findLetter == null) {
                log.info("해당 글은 존재하지 않음");
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }

            if (!id.equals(findLetter.getId())) {
                log.info("본인 글이 아님");
                return new ResponseEntity<>("NOT_WRITER", HttpStatus.BAD_REQUEST);
            }

            // 기존 게시물에 수정한 내용 덮어쓰기
            findLetter.modifyLetter(fanLetterWriteDto);


            List<FileInfo> fileInfoList = fileService.getFileInfoList("fanLetter",files);

            Map<String, Object> boardMap = new HashMap<>();

            boardMap.put("fanLetter", findLetter);
            boardMap.put("fileInfoList", fileInfoList);

            fanLetterService.modifyLetter(map);

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("NOT_LOGIN", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("FILE_UPLOAD_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MimeTypeNotMatchException e) {
            e.printStackTrace();
            return new ResponseEntity<>("MIMETYPE_ERROR", HttpStatus.BAD_REQUEST);
        } catch (FileNumbersLimitExceededException e) {
            e.printStackTrace();
            return new ResponseEntity<>("FILE_LIMIT_ERROR", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("MODIFY_OK", HttpStatus.OK);
    }

    @PostMapping("/remove/{num}")
    public ResponseEntity<String> deleteLetter(@PathVariable Long num, HttpServletRequest request) {
        try {
            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            fanLetterService.removeLetter(num, Name.ROLE_USER, id);

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("MEMBER_NOT_FOUND");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("NOT_YOUR_LETTER");
        }

        return ResponseEntity.ok()
                .body("REMOVE_OK");
    }

    private void modelInFileInfoList(Model model, Map<String, Object> map) {
        List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");
        ArrayList<FileDto> fileList = new ArrayList<>();
        for (FileInfo fileInfo : fileInfoList) {
            FileDto fileDto = new FileDto(fileInfo);
            fileList.add(fileDto);
        }

        model.addAttribute("fileInfoList", fileList);
    }

    private void modelInFanBoard(Model model, Map<String, Object> map) {
        FanBoard fanBoard = (FanBoard) map.get("fanLetter");
        FanLetterWriteDto fanLetterWriteDto = new FanLetterWriteDto(
                fanBoard.getTitle(), fanBoard.getContent()
        );
        model.addAttribute("fanLetter", fanLetterWriteDto);
    }


    private ResponseEntity<String> okResponseEntity() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", "OK");
        return webService.okResponseEntity(jsonObject);
    }
}