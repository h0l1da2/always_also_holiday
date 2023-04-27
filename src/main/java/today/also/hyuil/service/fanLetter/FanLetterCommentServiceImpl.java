package today.also.hyuil.service.fanLetter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.repository.fanLetter.FanLetterCommentRepository;
import today.also.hyuil.service.fanLetter.inter.FanLetterCommentService;

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
}
