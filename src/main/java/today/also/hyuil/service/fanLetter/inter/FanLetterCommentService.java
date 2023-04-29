package today.also.hyuil.service.fanLetter.inter;

import today.also.hyuil.domain.fanLetter.Comment;

import java.util.List;

public interface FanLetterCommentService {

    Comment writeComment(Comment comment);
    List<Comment> readComment(Long num);
}
