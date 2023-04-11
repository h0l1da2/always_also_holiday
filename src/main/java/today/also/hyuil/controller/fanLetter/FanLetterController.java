package today.also.hyuil.controller.fanLetter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.fanLetter.FanLetterWriteDto;
import today.also.hyuil.domain.dto.fanLetter.ImageDto;
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
import java.util.ArrayList;
import java.util.List;

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
    @PostMapping("/write")
    public String write(@RequestBody FanLetterWriteDto fanLetterWriteDto, HttpServletRequest request) {
        System.out.println("fanLetterWriteDto = " + fanLetterWriteDto);
        try {
            // 세션에서 memberId 가져오기
//            String memberId = getMemberIdInSession(request);
            String memberId = "aaaa1";

            if (!writeDtoNullCheck(fanLetterWriteDto)) {
                return "글 내용이 없음";
            }

            FanBoard fanBoard = new FanBoard(fanLetterWriteDto);

            // 이미지 파일이 존재할 경우
            List<FileInfo> fileInfoList = new ArrayList<>();
            if (isLetterHaveFiles(fanLetterWriteDto)) {
                System.out.println("fanLetterWriteDto.getImages() = " + fanLetterWriteDto.getImages());
                for (ImageDto imageDto : fanLetterWriteDto.getImages()) {
                    File file = new File(imageDto);
                    // path type mimeType
                    String imgMimeType = setImgMimeType(imageDto);
                    file.imgMimeType(imgMimeType);

                    file.filePath(filePath);
                    file.fileType(Type.IMAGE);

                    FileInfo fileInfo = new FileInfo(file);
                    fileInfo.whereFileIs(IsWhere.FAN_BOARD);

                    fileInfoList.add(fileInfo);
                }

            }

            FanBoard writeLetter = fanLetterService.writeLetter(memberId, fanBoard, fileInfoList);

            if (writeLetter.getId() == null) {
                return "작성 오류";
            }

        } catch (MimeTypeNotMatchException e) {
            return "이미지 파일 확장자를 확인해주세요";
        } catch (MemberNotFoundException me) {
            return "로그인을 해주세요";
        }
        return "작성 완료";
    }

    private String getMemberIdInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession(false);
        String memberId = (String) session.getAttribute("memberId");

        if (!StringUtils.hasText(memberId)) {
            throw new MemberNotFoundException();
        }

        return memberId;
    }

    private String setImgMimeType(ImageDto imageDto) throws MimeTypeNotMatchException {
        String imgMimeType = "";
        if (imageDto.getMimeType().contains("image/jpeg")) {
            imgMimeType = ".jpg";
        } else if (imageDto.getMimeType().contains("image/png")) {
            imgMimeType = ".png";
        } else if (imageDto.getMimeType().contains("image/gif")) {
            imgMimeType = ".gif";
        } else {
            // exception 만들기
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
        if (!StringUtils.hasText(fanLetterWriteDto.getMemberId())) {
            return false;
        }
        return true;
    }

    private boolean isLetterHaveFiles(FanLetterWriteDto fanLetterWriteDto) {
        if (fanLetterWriteDto.getImages().isEmpty()) {
            return false;
        }
        return true;
    }
}
