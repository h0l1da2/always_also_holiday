package today.also.hyuil.repository.market;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.market.*;
import today.also.hyuil.repository.market.inter.MarketSellRepository;

import jakarta.persistence.EntityManager;
import java.util.List;

import static today.also.hyuil.domain.market.QMarketSellCom.*;
import static today.also.hyuil.domain.member.QMember.member;

@Transactional
@Repository
public class MarketSellRepositoryImpl implements MarketSellRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public MarketSellRepositoryImpl(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    @Override
    public MarketSell insertMarket(MarketSell market) {
        em.persist(market);
        em.close();
        return market;
    }

    @Override
    public MarketSell seleteMarket(Long id) {
        MarketSell market = em.find(MarketSell.class, id);
        em.close();
        return market;
    }

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
        comment.getMember();
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
    public void updateMarketComRemover(Long id, MarketSellComRemover remover) {
        MarketSellCom marketSellCom = em.find(MarketSellCom.class, id);
        marketSellCom.itRemove(remover);
        em.close();
    }

    @Override
    public MarketSell seleteAndViewCntMarket(Long id) {
        MarketSell market = em.find(MarketSell.class, id);
        market.getMember();
        market.updateViewCnt();
        return market;
    }

    @Override
    public void updateMarket(Long id, MarketSell market) {
        MarketSell findMarket = em.find(MarketSell.class, id);
        findMarket.updateNewMarket(market);
        em.close();
    }

    @Override
    public void updateMarketForRemove(Long marketId, MarketSellRemover marketRemover) {
        MarketSell market = em.find(MarketSell.class, marketId);
        market.itRemove(marketRemover);
        em.close();
    }

    @Override
    public MarketSellRemover insertMarketRemover(MarketSellRemover marketRemover) {
        em.persist(marketRemover);
        em.close();
        return marketRemover;
    }
}

