package today.also.hyuil.repository.fanLetter;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.dto.fanLetter.BoardListDto;
import today.also.hyuil.domain.fanLetter.BoardRemover;
import today.also.hyuil.domain.fanLetter.FanBoard;

import jakarta.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.fanLetter.QFanBoard.fanBoard;

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

    public FanBoard selectAndViewCnt(Long num) {
        FanBoard fanBoard = em.find(FanBoard.class, num);
        fanBoard.updateViewCnt();
        return fanBoard;
    }

    public Page<BoardListDto> selectFanBoardList(Pageable pageable) {
        List<BoardListDto> resultList = query
                .select(Projections.constructor(BoardListDto.class,
                        fanBoard.id, fanBoard.title, fanBoard.member.nickname, fanBoard.uploadDate, fanBoard.view))
                .from(fanBoard)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query.select(fanBoard.count())
                .from(fanBoard)
                .fetchFirst();

        return new PageImpl<>(resultList, pageable, total);
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
