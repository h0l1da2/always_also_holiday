package today.also.hyuil.repository.fanLetter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.QComment;
import today.also.hyuil.domain.member.QMember;

import javax.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.fanLetter.QComment.*;
import static today.also.hyuil.domain.member.QMember.member;

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
}
