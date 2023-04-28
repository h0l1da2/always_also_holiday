package today.also.hyuil.controller.fanLetter;

import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import today.also.hyuil.domain.dto.fanLetter.CommentDto;
import today.also.hyuil.domain.dto.fanLetter.FanCommentWriteDto;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.ReplyType;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.service.fanLetter.inter.FanLetterCommentService;
import today.also.hyuil.service.member.inter.MemberJoinService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/fanLetter/comment")
@RestController
public class FanLetterCommentController {

    private final FanLetterCommentService fanLetterCommentService;
    private final MemberJoinService memberJoinService;

    public FanLetterCommentController(FanLetterCommentService fanLetterCommentService, MemberJoinService memberJoinService) {
        this.fanLetterCommentService = fanLetterCommentService;
        this.memberJoinService = memberJoinService;
    }

    @GetMapping("/write")
    public ResponseEntity<JsonObject> write(@RequestBody FanCommentWriteDto fanCommentWriteDto, HttpServletRequest request) {

        System.out.println("fanCommentWriteDto = " + fanCommentWriteDto);
        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */
        JsonObject jsonObject = new JsonObject();

        try {

            if (!writeDtoNullCheck(fanCommentWriteDto)) {
                System.out.println("comment NULL 들어옴");
                return badResponseEntity("COMMENT_NULL");
            }
            String memberId = getMemberIdInSession(request);
//            String memberId = "aaaa1";

            Member member = memberJoinService.findMyAccount(memberId);

            if (member == null) {
                return badResponseEntity("MEMBER_NOT_FOUND");
            }

            Comment comment = getComment(fanCommentWriteDto, member);

            // 작성 완료
            fanLetterCommentService.writeComment(comment);
            jsonObject.addProperty("data", "WRITE_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return badResponseEntity("MEMBER_NOT_FOUND");
        }

        return ResponseEntity.ok()
                .body(jsonObject);
    }

    private ResponseEntity<JsonObject> badResponseEntity(String cause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", cause);
        return ResponseEntity.badRequest()
                .body(jsonObject);
    }

    private Comment getComment(FanCommentWriteDto fanCommentWriteDto, Member member) {
        Comment comment = new Comment();
        if (fanCommentWriteDto.getCommentNum() == null) {
            comment.setCommentValues(member, ReplyType.COMMENT, fanCommentWriteDto);
        } else {
            comment.setCommentValues(member, ReplyType.REPLY, fanCommentWriteDto);
        }
        return comment;
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

    private boolean writeDtoNullCheck(FanCommentWriteDto fanCommentWriteDto) {
        if (fanCommentWriteDto == null) {
            return false;
        }
        if (fanCommentWriteDto.getLetterNum() == null) {
            return false;
        }
        if (!StringUtils.hasText(fanCommentWriteDto.getContent())) {
            return false;
        }
        return true;
    }


}
