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
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.File;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @PostMapping(value = "/write", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> write(@RequestPart(value = "image", required = false) List<MultipartFile> files,
                                @RequestPart(value = "fanLetterWriteDto") FanLetterWriteDto fanLetterWriteDto,
                                HttpServletRequest request) {

        System.out.println("fanLetterWriteDto = " + fanLetterWriteDto);
        System.out.println("files = " + files);

//         webPath 값을 지정하면 해당경로까지의 realPath를 추출하는 코드
//        String folderPath = request.getSession().getServletContext().getRealPath(filePath);
//
//        try {
//            // 세션에서 memberId 가져오기
//            String memberId = getMemberIdInSession(request);
////            String memberId = "aaaa1";
//
//            if (!writeDtoNullCheck(fanLetterWriteDto)) {
//                System.out.println("글 내용이 없음");
//                return new ResponseEntity<>("NOT_CONTENT", HttpStatus.BAD_REQUEST);
//            }
//
//            FanBoard fanBoard = new FanBoard(fanLetterWriteDto);
//
//            // 이미지 파일이 존재할 경우
//            List<FileInfo> fileInfoList = new ArrayList<>();
//            if (isLetterHaveFiles(files)) {
//                for (MultipartFile multipartFile : files) {
//
//                    String fileUuid = UUID.randomUUID().toString();
//                    // path type mimeType
//                    String imgMimeType = setImgMimeType(multipartFile);
//                    File file = new File(multipartFile);
//                    file.fileUUID(fileUuid);
//
//                    // 파일 저장 -> java.io.File (path/UUID.jpg)
//                    saveFile(multipartFile, imgMimeType, fileUuid);
//
//                    file.imgMimeType(imgMimeType);
//
//                    file.filePath(filePath);
//                    file.fileType(Type.IMAGE);
//
//                    FileInfo fileInfo = new FileInfo(file);
//                    fileInfo.whereFileIs(IsWhere.FAN_BOARD);
//
//                    fileInfoList.add(fileInfo);
//                }
//
//            }
//
//            FanBoard writeLetter = fanLetterService.writeLetter(memberId, fanBoard, fileInfoList);
//
//            if (writeLetter.getId() == null) {
//                System.out.println("작성 오류");
//                return new ResponseEntity<>("WRITE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//
//        } catch (MimeTypeNotMatchException e) {
//            System.out.println("이미지 파일 확장자 다름");
//            return new ResponseEntity<>("MIMETYPE_ERROR", HttpStatus.BAD_REQUEST);
//        } catch (MemberNotFoundException me) {
//            System.out.println("로그인이 안 됨");
//            return new ResponseEntity<>("NOT_LOGIN", HttpStatus.BAD_REQUEST);
//        } catch (IOException e) {
//            System.out.println("파일 업로드 에러");
//            e.printStackTrace();
//            return new ResponseEntity<>("FILE_UPLOAD_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        System.out.println("!!!!!!!!!!!!!!!!");
        return new ResponseEntity<>("WRITE_OK", HttpStatus.OK);
    }

    private void saveFile(MultipartFile multipartFile, String mimeType, String fileUuid) throws IOException {
        var fileIo = new java.io.File(filePath+ fileUuid + mimeType);
        multipartFile.transferTo(fileIo);
    }

    private String getMemberIdInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        String memberId = null;
        try {
            memberId = (String) session.getAttribute("memberId");
        } catch (NullPointerException e) {
            e.printStackTrace();
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
