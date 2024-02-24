package today.also.hyuil.market.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketCom;
import today.also.hyuil.market.domain.MarketComRemover;
import today.also.hyuil.market.domain.MarketRemover;

import java.util.List;

import static today.also.hyuil.domain.market.QMarketCom.marketCom;
import static today.also.hyuil.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MarketRepositoryImpl implements MarketRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    @Override
    public List<MarketCom> selectMarketComments(Long id) {
        return query.select(marketCom)
                .from(marketCom)
                .where(marketCom.market.id.eq(id))
                .leftJoin(marketCom.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    @Override
    public MarketCom insertBuyComment(MarketCom comment) {
        em.persist(comment);
        em.close();
        return comment;
    }

    @Override
    public MarketCom findByIdMarketCom(Long commentId) {
        MarketCom comment = em.find(MarketCom.class, commentId);
        comment.getMember().getId();
        em.close();
        return comment;
    }

    @Override
    public MarketComRemover insertMarketComRemover(MarketComRemover marketComRemover) {
        em.persist(marketComRemover);
        em.close();
        return marketComRemover;
    }

    @Override
    public Market seleteAndViewCntMarket(Long id) {
        Market market = em.find(Market.class, id);
        market.getMember().getId();
        market.updateViewCnt();
        em.close();
        return market;
    }

    @Override
    public MarketRemover insertMarketRemover(MarketRemover marketRemover) {
        em.persist(marketRemover);
        em.close();
        return marketRemover;
    }
}
