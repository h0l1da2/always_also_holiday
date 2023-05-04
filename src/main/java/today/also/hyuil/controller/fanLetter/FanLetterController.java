package today.also.hyuil.controller.fanLetter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.dto.fanLetter.*;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.Files;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.service.fanLetter.inter.FanLetterCommentService;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@Controller
@RequestMapping("/fanLetter")
@PropertySource("classpath:application.yml")
public class FanLetterController {

    private final WebService webService;
    private final FanLetterService fanLetterService;
    private final FanLetterCommentService fanLetterCommentService;

    @Value("${file.fan.letter.path}")
    private String filePath;
    public FanLetterController(WebService webService, FanLetterService fanLetterService, FanLetterCommentService fanLetterCommentService) {
        this.webService = webService;
        this.fanLetterService = fanLetterService;
        this.fanLetterCommentService = fanLetterCommentService;
    }

    @GetMapping
    public String fanLetterList(@PageableDefault Pageable pageable, Model model) {

        Page<FanBoard> fanBoards = fanLetterService.listMain(pageable);
        Page<BoardListDto> fanListDto =
                fanBoards.map(fanBoard -> new BoardListDto(fanBoard));

        model.addAttribute("fanLetterList", fanListDto);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "fanLetter/boardList";
    }

    @GetMapping("/{num}")
    public String fanLetter(@PathVariable Long num, Model model, HttpServletRequest request) {
        // 글
        Map<String, Object> map = fanLetterService.readLetter(num);

        FanBoard fanBoard = (FanBoard) map.get("fanLetter");
        List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");

        List<String> filePaths = new ArrayList<>();

        for (FileInfo fileInfo : fileInfoList) {
            String filePath = pathSubstring(fileInfo);
            filePaths.add(filePath);
        }

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
            String nickname = webService.getNicknameInSession(request);
            if (nickname.equals(fanBoard.getMember().getNickname())) {
                model.addAttribute("nickname", nickname);
            }
        } catch (MemberNotFoundException e) {
            System.out.println("로그인이 안 되어 있음");
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
//      webPath 값을 지정하면 해당경로까지의 realPath를 추출하는 코드
//        String folderPath = request.getSession().getServletContext().getRealPath(filePath);

        try {
            // 세션에서 memberId 가져오기
            Long id = webService.getIdInSession(request);
//            String memberId = "aaaa1";

            if (!writeDtoNullCheck(fanLetterWriteDto)) {
                System.out.println("글 내용이 없음");
                return new ResponseEntity<>("NOT_CONTENT", HttpStatus.BAD_REQUEST);
            }

            FanBoard fanBoard = new FanBoard(fanLetterWriteDto);

            // 이미지 파일이 존재할 경우
            // 여기에서 뭔가 문제가 발생
            List<FileInfo> fileInfoList = new ArrayList<>();
            if (isLetterHaveFiles(files)) {
                for (MultipartFile multipartFile : files) {

                    Files file = getFiles(multipartFile);

                    FileInfo fileInfo = new FileInfo(file);
                    fileInfo.whereFileIs(IsWhere.FAN_BOARD);

                    fileInfoList.add(fileInfo);
                }
            }

            FanBoard writeLetter = fanLetterService.writeLetter(id, fanBoard, fileInfoList);

            if (writeLetter.getId() == null) {
                System.out.println("작성 오류");
                return new ResponseEntity<>("WRITE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (MimeTypeNotMatchException e) {
            e.printStackTrace();
            System.out.println("이미지 파일 확장자 다름");
            return new ResponseEntity<>("MIMETYPE_ERROR", HttpStatus.BAD_REQUEST);
        } catch (MemberNotFoundException me) {
            me.printStackTrace();
            System.out.println("로그인이 안 됨");
            return new ResponseEntity<>("NOT_LOGIN", HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 업로드 에러");
            e.printStackTrace();
            return new ResponseEntity<>("FILE_UPLOAD_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("WRITE_OK", HttpStatus.OK);
    }

    @GetMapping("/modify/{num}")
    public String modify(@PathVariable Long num, Model model, HttpServletRequest request) {
        try {
            Long id = webService.getIdInSession(request);
//            String memberId = "aaaa1";
            /**
             * 1. 본인 글인지 검증
             * 2. 본인 글이 맞다면 내용 보여줌
             * 3. 사진은...? 일단 글만 보여주는 걸로 -> 사진도
             */

            Map<String, Object> map = fanLetterService.findLetter(id, num);
            modelInFanBoard(model, map);

            if (map.containsKey("fileInfoList")) {
                modelInFileInfoList(model, map);
            }

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/fanLetter";
        }

        return "fanLetter/modifyPage";
    }

    @ResponseBody
    @PostMapping(value = "/modify/{num}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> modify(@PathVariable Long num, HttpServletRequest request,
                                         @RequestPart(value = "image", required = false) List<MultipartFile> files,
                                         @RequestPart(value = "fanLetterWriteDto") FanLetterWriteDto fanLetterWriteDto) {
        try {
            Long id = webService.getIdInSession(request);
//            String memberId = "aaaa1";

            if (!writeDtoNullCheck(fanLetterWriteDto)) {
                System.out.println("글 내용이 없음");
                return new ResponseEntity<>("NOT_CONTENT", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> map = fanLetterService.readLetter(num);

            FanBoard findLetter = (FanBoard) map.get("fanLetter");

            if (findLetter == null) {
                System.out.println("해당 글은 존재하지 않음");
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }

            if (!id.equals(findLetter.getId())) {
                System.out.println("본인이 쓴 글이 아님");
                return new ResponseEntity<>("NOT_WRITER", HttpStatus.BAD_REQUEST);
            }

            // 기존 게시물에 수정한 내용 덮어쓰기
            findLetter.modifyLetter(fanLetterWriteDto);


            List<FileInfo> fileInfoList = new ArrayList<>();
            if (isLetterHaveFiles(files)) {
                for (MultipartFile multipartFile : files) {

                    Files file = getFiles(multipartFile);

                    FileInfo fileInfo = new FileInfo(file);
                    fileInfo.whereFileIs(IsWhere.FAN_BOARD);

                    fileInfoList.add(fileInfo);
                }
            }


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
//            String memberId = "aaaa1";

            fanLetterService.removeLetter(num, "MEMBER", id);

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

    private void saveFile(MultipartFile multipartFile, String mimeType, String fileUuid) throws IOException {
        File fileIo = new File(filePath + fileUuid + mimeType);
        multipartFile.transferTo(fileIo);
    }

    private Files getFiles(MultipartFile multipartFile) throws MimeTypeNotMatchException, IOException {
        String fileUuid = UUID.randomUUID().toString();
        // path type mimeType
        String imgMimeType = setImgMimeType(multipartFile);
        Files file = new Files(multipartFile);
        file.fileUUID(fileUuid);

        // 파일 저장 -> java.io.File (path/UUID.jpg)
        saveFile(multipartFile, imgMimeType, fileUuid);

        file.imgMimeType(imgMimeType);

        file.filePath(filePath);
        file.fileType(Type.IMAGE);
        return file;
    }

    private String setImgMimeType(MultipartFile multipartFile) throws MimeTypeNotMatchException {
        String imgMimeType = "";
        if (multipartFile.getContentType().contains("image/jpeg")) {
            imgMimeType = ".jpg";
        } else if (multipartFile.getContentType().contains("image/png")) {
            imgMimeType = ".png";
        } else if (multipartFile.getContentType().contains("image/gif")) {
            imgMimeType = ".gif";
        } else {
            // exception 만들기
            System.out.println("올바른 확장자가 아닙니다");
            throw new MimeTypeNotMatchException("올바른 확장자가 아닙니다");
        }
        return imgMimeType;
    }

    private String pathSubstring(FileInfo fileInfo) {
        String filePath = fileInfo.getFile().getPath() + fileInfo.getFile().getUuid() + fileInfo.getFile().getMimeType();
        int startPath = "/Users/holiday/IdeaProjects/also_hyuil/src/main/resources".length();
        filePath = filePath.substring(startPath);
        return filePath;
    }

    private boolean writeDtoNullCheck(FanLetterWriteDto fanLetterWriteDto) {
        if (fanLetterWriteDto == null) {
            return false;
        }
        if (!StringUtils.hasText(fanLetterWriteDto.getTitle())) {
            return false;
        }
        if (!StringUtils.hasText(fanLetterWriteDto.getContent())) {
            return false;
        }
        return true;
    }

    private boolean isLetterHaveFiles(List<MultipartFile> files) {
        if (files == null) {
            return false;
        }
        return true;
    }

}
