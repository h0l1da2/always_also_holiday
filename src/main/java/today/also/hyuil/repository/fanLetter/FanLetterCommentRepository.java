package today.also.hyuil.repository.fanLetter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.Comment;

import javax.persistence.EntityManager;

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
}
