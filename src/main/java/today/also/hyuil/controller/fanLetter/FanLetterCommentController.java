package today.also.hyuil.controller.fanLetter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.config.security.CustomUserDetails;
import today.also.hyuil.domain.dto.fanLetter.CommentDto;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.fanLetter.ReplyType;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.service.fanLetter.inter.FanLetterCommentService;
import today.also.hyuil.service.fanLetter.inter.FanLetterService;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/fanLetter/comment")
@RestController
public class FanLetterCommentController {

    private final WebService webService;
    private final FanLetterService fanLetterService;
    private final FanLetterCommentService fanLetterCommentService;
    private final MemberJoinService memberJoinService;

    public FanLetterCommentController(WebService webService, FanLetterService fanLetterService, FanLetterCommentService fanLetterCommentService, MemberJoinService memberJoinService) {
        this.webService = webService;
        this.fanLetterService = fanLetterService;
        this.fanLetterCommentService = fanLetterCommentService;
        this.memberJoinService = memberJoinService;
    }

    // TODO 댓글 비동기로 직접 넣도록 수정

    @GetMapping("/{num}")
    public List<CommentDto> showComments(@PathVariable Long num) {
        List<Comment> commentList = fanLetterCommentService.readComment(num);

        List<CommentDto> comments = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentDto commentDto = new CommentDto(comment);
            comments.add(commentDto);
        }

        return comments;
    }

    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody CommentWriteDto commentWriteDto, HttpServletRequest request) {

        System.out.println("fanCommentWriteDto = " + commentWriteDto);
        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */
        JsonObject jsonObject = new JsonObject();

        try {

            if (!writeDtoNullCheck(commentWriteDto)) {
                System.out.println("comment NULL 들어옴");
                return badResponseEntity("COMMENT_NULL");
            }
            Long id = webService.getIdInSession(request);
//            String memberId = "aaaa1";

            Member member = memberJoinService.findMyAccount(id);

            if (member == null) {
                return badResponseEntity("MEMBER_NOT_FOUND");
            }

            Map<String, Object> map = fanLetterService.readLetter(commentWriteDto.getLetterNum());
            FanBoard fanBoard = (FanBoard) map.get("fanLetter");
            Comment comment = getComment(commentWriteDto, member, fanBoard);

            // 작성 완료
            fanLetterCommentService.writeComment(comment);
            jsonObject.addProperty("data", "WRITE_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return badResponseEntity("MEMBER_NOT_FOUND");
        }
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> remove(@RequestBody CommentDto commentDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null) {
                return ResponseEntity.badRequest()
                        .body("MEMBER_NOT_FOUND");
            }
            fanLetterCommentService.removeComment(commentDto.getId(), userDetails.getUsername());
//            fanLetterCommentService.removeComment(commentDto.getId(), "aaaa1");
        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("COMMENT_NOT_FOUND");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("NOT_YOUR_COMMENT");
        } catch (NumberFormatException e) {
            System.out.println("타입 변환 에러(코멘트 파라미터가 숫자 타입이 아님)");
            return ResponseEntity.badRequest()
                    .body("BAD_COMMENT_ID");
        }


        return ResponseEntity.ok()
                .body("REMOVE_OK");
    }

    private ResponseEntity<String> badResponseEntity(String cause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", cause);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.badRequest()
                .body(jsonResponse);
    }

    private Comment getComment(CommentWriteDto commentWriteDto, Member member, FanBoard fanBoard) {
        Comment comment = new Comment();
        if (commentWriteDto.getCommentNum() == null) {
            comment.setCommentValues(member, ReplyType.COMMENT, commentWriteDto, fanBoard);
        } else {
            comment.setCommentValues(member, ReplyType.REPLY, commentWriteDto, fanBoard);
        }
        return comment;
    }

    private boolean writeDtoNullCheck(CommentWriteDto commentWriteDto) {
        if (commentWriteDto == null) {
            return false;
        }
        if (commentWriteDto.getLetterNum() == null) {
            return false;
        }
        if (!StringUtils.hasText(commentWriteDto.getContent())) {
            return false;
        }
        return true;
    }


}
