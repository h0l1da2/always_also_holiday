package today.also.hyuil.repository.fanLetter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.FanBoard;

import javax.persistence.EntityManager;

@Transactional
@Repository
public class FanLetterRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public FanLetterRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public FanBoard insertFanBoard(FanBoard fanBoard) {
        em.persist(fanBoard);
        em.close();
        return fanBoard;
    }

    public void selectFanBoardList() {
        query.select();
    }
}
