package today.also.hyuil.fanletter.service;

import com.amazonaws.services.kms.model.NotFoundException;
import today.also.hyuil.fanletter.domain.Comment;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface FanLetterCommentService {

    Comment writeComment(Comment comment);
    List<Comment> readComment(Long num);
    void removeComment(Long commentId, Long memberId) throws NotFoundException, AccessDeniedException;
}
