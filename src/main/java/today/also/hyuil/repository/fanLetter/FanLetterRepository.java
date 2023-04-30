package today.also.hyuil.repository.fanLetter;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.fanLetter.BoardRemover;
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

    public FanBoard selectFanBoard(Long num) {
        FanBoard fanBoard = em.find(FanBoard.class, num);
        em.close();
        return fanBoard;
    }

    public void selectFanBoardList() {
        query.select();
    }

    public void modifyFanBoard(FanBoard fanBoard) {
    }

    public BoardRemover insertBoardRemover(BoardRemover boardRemover) {
        em.persist(boardRemover);
        em.close();
        return boardRemover;
    }

    public void updateLetterRemover(Long boardNum, BoardRemover remover) {
        FanBoard findBoard = em.find(FanBoard.class, boardNum);
        findBoard.deleteFanLetter(remover);
        em.close();
    }
}
