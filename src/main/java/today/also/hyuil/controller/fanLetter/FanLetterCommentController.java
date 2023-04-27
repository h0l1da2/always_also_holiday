package today.also.hyuil.controller.fanLetter;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.also.hyuil.domain.dto.fanLetter.FanCommentWriteDto;
import today.also.hyuil.service.fanLetter.inter.FanLetterCommentService;

@RequestMapping("/fanLetter/comment")
@RestController
public class FanLetterCommentController {

    private final FanLetterCommentService fanLetterCommentService;

    public FanLetterCommentController(FanLetterCommentService fanLetterCommentService) {
        this.fanLetterCommentService = fanLetterCommentService;
    }

    @GetMapping("/write")
    public ResponseEntity<JsonObject> write(@RequestBody FanCommentWriteDto fanCommentWriteDto) {

        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */

        return null;
    }


}
