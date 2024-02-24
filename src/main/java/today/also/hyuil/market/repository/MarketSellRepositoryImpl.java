package today.also.hyuil.market.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import today.also.hyuil.market.domain.MarketSell;
import today.also.hyuil.market.domain.MarketSellCom;
import today.also.hyuil.market.domain.MarketSellComRemover;
import today.also.hyuil.market.domain.MarketSellRemover;

import java.util.List;

import static today.also.hyuil.domain.market.QMarketSellCom.marketSellCom;
import static today.also.hyuil.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MarketSellRepositoryImpl implements MarketSellRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public List<MarketSellCom> selectMarketComments(Long id) {
        return query.select(marketSellCom)
                .from(marketSellCom)
                .where(marketSellCom.market.id.eq(id))
                .leftJoin(marketSellCom.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    @Override
    public MarketSellCom insertBuyComment(MarketSellCom comment) {
        em.persist(comment);
        em.close();
        return comment;
    }

    @Override
    public MarketSellCom findByIdMarketCom(Long commentId) {
        MarketSellCom comment = em.find(MarketSellCom.class, commentId);
        comment.getMember().getId();
        em.close();
        return comment;
    }

    @Override
    public MarketSellComRemover insertMarketComRemover(MarketSellComRemover marketComRemover) {
        em.persist(marketComRemover);
        em.close();
        return marketComRemover;
    }

    @Override
    public MarketSell seleteAndViewCntMarket(Long id) {
        MarketSell market = em.find(MarketSell.class, id);
        market.getMember().getId();
        market.updateViewCnt();
        em.close();
        return market;
    }

    @Override
    public MarketSellRemover insertMarketRemover(MarketSellRemover marketRemover) {
        em.persist(marketRemover);
        em.close();
        return marketRemover;
    }
}

