package today.also.hyuil.controller.fanLetter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.fanLetter.FanLetterWriteDto;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.file.File;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.file.IsWhere;
import today.also.hyuil.domain.file.Type;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;

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
    public String write(@RequestBody FanLetterWriteDto fanLetterWriteDto) {

        if (!writeDtoNullCheck(fanLetterWriteDto)) {
            return "글 내용이 없음";
        }

        FanBoard fanBoard = new FanBoard(fanLetterWriteDto);
        try {

            // 이미지 파일이 존재할 경우
            if (isSetFile(fanLetterWriteDto)) {
                File file = new File(fanLetterWriteDto);

                // path type mimeType
                String imgMimeType = setImgMimeType(fanLetterWriteDto);
                file.imgMimeType(imgMimeType);


                file.filePath(filePath);
                file.fileType(Type.IMAGE);

                FileInfo fileInfo = new FileInfo(file);
                fileInfo.whereFileIs(IsWhere.FAN_BOARD);

                fanBoard.letterHaveFile(fileInfo);
            }

            FanBoard writeLetter = fanLetterService.writeLetter(fanLetterWriteDto.getMemberId(), fanBoard);

            if (writeLetter == null) {
                return "작성 오류";
            }

        } catch (MimeTypeNotMatchException e) {
            return "이미지 파일 확장자를 확인해주세요";
        } catch (MemberNotFoundException me) {
            return "로그인을 해주세요";
        }
        return "작성 완료";
    }

    private String setImgMimeType(FanLetterWriteDto fanLetterWriteDto) throws MimeTypeNotMatchException {
        String imgMimeType = "";
        if (fanLetterWriteDto.getMimeType().contains("image/jpeg")) {
            imgMimeType = ".jpg";
        } else if (fanLetterWriteDto.getMimeType().contains("image/png")) {
            imgMimeType = ".png";
        } else if (fanLetterWriteDto.getMimeType().contains("image/gif")) {
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

    private boolean isSetFile(FanLetterWriteDto fanLetterWriteDto) {
        if (!StringUtils.hasText(fanLetterWriteDto.getFileName())) {
            return false;
        }
        if (!StringUtils.hasText(fanLetterWriteDto.getMimeType())) {
            return false;
        }
        if (fanLetterWriteDto.getFileSize() == null) {
            return false;
        }
        return true;
    }
}
