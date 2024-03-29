package today.also.hyuil.fanletter.service;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.fanletter.domain.Comment;
import today.also.hyuil.fanletter.domain.CommentRemover;
import today.also.hyuil.fanletter.repository.FanLetterCommentRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Transactional
@Service
public class FanLetterCommentServiceImpl implements FanLetterCommentService {

    private final FanLetterCommentRepository commentRepository;

    public FanLetterCommentServiceImpl(FanLetterCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment writeComment(Comment comment) {
        return commentRepository.insertComment(comment);
    }

    @Override
    public List<Comment> readComment(Long num) {
        return commentRepository.selectComments(num);
    }

    @Override
    public void removeComment(Long commentId, Long memberId) throws NotFoundException, AccessDeniedException {
        Comment comment = commentRepository.selectComment(commentId);

        if (comment == null) {
            throw new NotFoundException("해당 댓글을 찾을 수 없습니다");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("댓글을 쓴 본인만 삭제가 가능합니다");
        }

        CommentRemover remover = commentRepository.insertCommentRemover(
                new CommentRemover(comment.getMember()));

        commentRepository.updateRemover(comment.getId(), remover);
    }
}
