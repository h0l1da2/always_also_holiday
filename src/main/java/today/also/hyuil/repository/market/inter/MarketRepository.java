package today.also.hyuil.repository.market.inter;

import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.domain.market.MarketComRemover;
import today.also.hyuil.domain.market.MarketRemover;

import java.util.List;

public interface MarketRepository {
    Market insertMarket(Market market);

    Market seleteMarket(Long id);

    List<MarketCom> selectMarketComments(Long id);

    MarketCom insertBuyComment(MarketCom comment);

    MarketCom findByIdMarketCom(Long commentId);

    MarketComRemover insertMarketComRemover(MarketComRemover marketComRemover);

    void updateMarketComRemover(Long id, MarketComRemover remover);

    Market seleteAndViewCntMarket(Long id);

    void updateMarket(Long id, Market market);

    void updateMarketForRemove(Long marketId, MarketRemover marketRemover);

    MarketRemover insertMarketRemover(MarketRemover marketRemover);
}
