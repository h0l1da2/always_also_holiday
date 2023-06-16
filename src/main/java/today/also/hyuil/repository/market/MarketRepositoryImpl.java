package today.also.hyuil.repository.market;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.market.*;
import today.also.hyuil.repository.market.inter.MarketRepository;

import jakarta.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.market.QMarketCom.marketCom;
import static today.also.hyuil.domain.member.QMember.member;

// TODO 서비스에 붙여봐 ?
@Transactional
@Repository
public class MarketRepositoryImpl implements MarketRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public MarketRepositoryImpl(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    @Override
    public Market insertMarket(Market market) {
        em.persist(market);
        em.close();
        return market;
    }

    @Override
    public Market seleteMarket(Long id) {
        Market market = em.find(Market.class, id);
        em.close();
        return market;
    }

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
        comment.getMember();
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
    public void updateMarketComRemover(Long id, MarketComRemover remover) {
        MarketCom MarketCom = em.find(MarketCom.class, id);
        MarketCom.itRemove(remover);
        em.close();
    }

    @Override
    public Market seleteAndViewCntMarket(Long id) {
        Market market = em.find(Market.class, id);
        market.getMember();
        market.updateViewCnt();
        return market;
    }

    @Override
    public void updateMarket(Long id, Market market) {
        Market findMarket = em.find(Market.class, id);
        findMarket.updateNewMarket(market);
        em.close();
    }

    @Override
    public void updateMarketForRemove(Long marketId, MarketRemover marketRemover) {
        Market market = em.find(Market.class, marketId);
        market.itRemove(marketRemover);
        em.close();
    }

    @Override
    public MarketRemover insertMarketRemover(MarketRemover marketRemover) {
        em.persist(marketRemover);
        em.close();
        return marketRemover;
    }
}
