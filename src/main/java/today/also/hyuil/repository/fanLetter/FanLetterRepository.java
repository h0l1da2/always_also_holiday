package today.also.hyuil.repository.fanLetter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import today.also.hyuil.domain.fanLetter.BoardRemover;
import today.also.hyuil.domain.fanLetter.FanBoard;

@Repository
@RequiredArgsConstructor
public class FanLetterRepository {

    private final EntityManager em;

    public FanBoard selectAndViewCnt(Long num) {
        FanBoard fanBoard = em.find(FanBoard.class, num);
        fanBoard.updateViewCnt();
        em.close();
        return fanBoard;
    }

    public BoardRemover insertBoardRemover(BoardRemover boardRemover) {
        em.persist(boardRemover);
        em.close();
        return boardRemover;
    }

}
