package today.also.hyuil.service.fanLetter.inter;

import com.amazonaws.services.kms.model.NotFoundException;
import today.also.hyuil.domain.fanLetter.Comment;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface FanLetterCommentService {

    Comment writeComment(Comment comment);
    List<Comment> readComment(Long num);
    void removeComment(Long commentId, Long memberId) throws NotFoundException, AccessDeniedException;
}
