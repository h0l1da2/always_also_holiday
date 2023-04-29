package today.also.hyuil.repository.fanLetter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.QComment;

import javax.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.fanLetter.QComment.*;

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

    public List<Comment> selectComment(Long num) {
        return null;
    }
}
