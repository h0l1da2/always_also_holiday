package today.also.hyuil.repository.market;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketComBuy;

import javax.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.market.QMarketComBuy.marketComBuy;
import static today.also.hyuil.domain.member.QMember.member;

@Transactional
@Repository
public class MarketRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MarketRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Market insertMarket(Market market) {
        em.persist(market);
        em.close();
        return market;
    }

    public Market seleteMarket(Long id) {
        Market market = em.find(Market.class, id);
        em.close();
        return market;
    }

    public List<MarketComBuy> selectMarketBuyComments(Long id) {
        return query.select(marketComBuy)
                .from(marketComBuy)
                .where(marketComBuy.market.id.eq(id))
                .leftJoin(marketComBuy.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    public MarketComBuy insertBuyComment(MarketComBuy comment) {
        em.persist(comment);
        em.close();
        return comment;
    }
}
