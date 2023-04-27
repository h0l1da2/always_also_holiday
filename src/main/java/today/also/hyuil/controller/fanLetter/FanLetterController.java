package today.also.hyuil.controller.fanLetter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.dto.fanLetter.FanLetterWriteDto;
import today.also.hyuil.domain.dto.fanLetter.FileDto;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.Files;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.exception.FileNumbersLimitExceededException;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/fanLetter")
@PropertySource("classpath:application.yml")
public class FanLetterController {

    private final FanLetterService fanLetterService;

    @Value("${file.fan.letter.path}")
    private String filePath;
    public FanLetterController(FanLetterService fanLetterService) {
        this.fanLetterService = fanLetterService;
    }

    @GetMapping
    public String fanLetterList(Model model) {

        return "fanLetter/boardList";
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
            String memberId = getMemberIdInSession(request);
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

            FanBoard writeLetter = fanLetterService.writeLetter(memberId, fanBoard, fileInfoList);

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
            String memberId = getMemberIdInSession(request);
//            String memberId = "aaaa1";
            /**
             * 1. 본인 글인지 검증
             * 2. 본인 글이 맞다면 내용 보여줌
             * 3. 사진은...? 일단 글만 보여주는 걸로 -> 사진도
             */

            Map<String, Object> map = fanLetterService.readLetter(memberId, num);
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
            String memberId = getMemberIdInSession(request);
//            String memberId = "aaaa1";

            if (!writeDtoNullCheck(fanLetterWriteDto)) {
                System.out.println("글 내용이 없음");
                return new ResponseEntity<>("NOT_CONTENT", HttpStatus.BAD_REQUEST);
            }

            FanBoard findLetter = fanLetterService.findLetter(num);

            if (findLetter == null) {
                System.out.println("해당 글은 존재하지 않음");
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }

            if (!memberId.equals(findLetter.getMember().getMemberId())) {
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


            Map<String, Object> map = new HashMap<>();

            map.put("fanBoard", findLetter);
            map.put("fileInfoList", fileInfoList);

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
        FanBoard fanBoard = (FanBoard) map.get("fanBoard");
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

    private String getMemberIdInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        String memberId = null;
        try {
            memberId = (String) session.getAttribute("memberId");
        } catch (NullPointerException e) {
            throw new MemberNotFoundException("세션에 아이디가 없음");
        }
        return memberId;
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
