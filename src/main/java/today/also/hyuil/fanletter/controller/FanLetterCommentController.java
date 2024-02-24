package today.also.hyuil.fanletter.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.fanletter.dto.CommentDto;
import today.also.hyuil.fanletter.dto.CommentWriteDto;
import today.also.hyuil.fanletter.domain.Comment;
import today.also.hyuil.fanletter.domain.FanBoard;
import today.also.hyuil.fanletter.domain.ReplyType;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.MemberNotFoundException;
import today.also.hyuil.fanletter.service.FanLetterCommentService;
import today.also.hyuil.fanletter.service.FanLetterService;
import today.also.hyuil.member.service.MemberJoinService;
import today.also.hyuil.common.web.WebService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/fanLetter/comment")
@RestController
@Slf4j
@RequiredArgsConstructor
public class FanLetterCommentController {

    private final WebService webService;
    private final FanLetterService fanLetterService;
    private final FanLetterCommentService fanLetterCommentService;
    private final MemberJoinService memberJoinService;

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
    public ResponseEntity<String> write(@RequestBody @Valid CommentWriteDto commentWriteDto, HttpServletRequest request) {
        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */
        JsonObject jsonObject = new JsonObject();

        try {

            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            Member member = memberJoinService.findMyAccount(id);

            if (member == null) {
                log.info("멤버가 없습니다. 로그인 안 되어 있음.");
                return webService.badResponseEntity("MEMBER_NOT_FOUND");
            }

            Map<String, Object> map = fanLetterService.readLetter(commentWriteDto.getBoardNum());
            FanBoard fanBoard = (FanBoard) map.get("fanLetter");
            Comment comment = getComment(commentWriteDto, member, fanBoard);

            // 작성 완료
            fanLetterCommentService.writeComment(comment);
            jsonObject.addProperty("data", "WRITE_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        }
        return webService.okResponseEntity(jsonObject);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam Long id, HttpServletRequest request) {
        try {
            Long memberId = webService.getIdInSession(request);
            fanLetterCommentService.removeComment(id, memberId);
//            fanLetterCommentService.removeComment(commentDto.getId(), 1L);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("COMMENT_NOT_FOUND");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("NOT_YOUR_COMMENT");
        } catch (NumberFormatException e) {
            log.info("타입 변환 에러(코멘트 파라미터가 숫자 타입이 아님)");
            return ResponseEntity.badRequest()
                    .body("BAD_COMMENT_ID");
        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("MEMBER_NOT_FOUND");
        }


        return ResponseEntity.ok()
                .body("REMOVE_OK");
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


}
