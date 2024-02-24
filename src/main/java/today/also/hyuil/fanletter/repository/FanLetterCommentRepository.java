package today.also.hyuil.fanletter.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.fanletter.domain.Comment;
import today.also.hyuil.fanletter.domain.CommentRemover;

import java.util.List;

import static today.also.hyuil.fanletter.domain.QComment.comment;
import static today.also.hyuil.member.domain.QMember.member;

@Transactional
@Repository
public class FanLetterCommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FanLetterCommentRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Comment insertComment(Comment comment) {
        em.persist(comment);
        em.close();
        return comment;
    }

    public List<Comment> selectComments(Long num) {
        return query.select(comment)
                .from(comment)
                .where(comment.fanBoard.id.eq(num))
                .leftJoin(comment.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    public Comment selectComment(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        comment.getMember();
        em.close();
        return comment;
    }

    public void deleteComment(Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
    }

    public CommentRemover insertCommentRemover(CommentRemover commentRemover) {
        em.persist(commentRemover);
        em.close();
        return commentRemover;
    }

    public void updateRemover(Long id, CommentRemover commentRemover) {
        Comment findComment = em.find(Comment.class, id);
        findComment.removeComment(commentRemover);
        em.close();
    }
}
