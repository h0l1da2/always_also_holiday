package today.also.hyuil.repository.market;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.error.Mark;
import today.also.hyuil.domain.market.MarketComRemover;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;

import javax.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.market.QMarketCom.marketCom;
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

    public List<MarketCom> selectMarketBuyComments(Long id) {
        return query.select(marketCom)
                .from(marketCom)
                .where(marketCom.market.id.eq(id))
                .leftJoin(marketCom.member, member)
                .fetchJoin()
                .distinct()
                .fetch();
    }

    public MarketCom insertBuyComment(MarketCom comment) {
        em.persist(comment);
        em.close();
        return comment;
    }

    public MarketCom findByIdMarketCom(Long commentId) {
        MarketCom comment = em.find(MarketCom.class, commentId);
        comment.getMember();
        em.close();
        return comment;
    }

    public MarketComRemover insertMarketComRemover(MarketComRemover marketComRemover) {
        em.persist(marketComRemover);
        em.close();
        return marketComRemover;
    }

    public void updateMarketComRemover(Long id, MarketComRemover remover) {
        MarketCom MarketCom = em.find(MarketCom.class, id);
        MarketCom.itRemove(remover);
        em.close();
    }

    public Market seleteAndViewCntMarket(Long id) {
        Market market = em.find(Market.class, id);
        market.updateViewCnt();
        return market;
    }
}
