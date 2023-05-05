package today.also.hyuil.repository.market.inter;

import today.also.hyuil.domain.market.MarketSell;
import today.also.hyuil.domain.market.MarketSellCom;
import today.also.hyuil.domain.market.MarketSellComRemover;
import today.also.hyuil.domain.market.MarketSellRemover;

import java.util.List;

public interface MarketSellRepository {
    MarketSell insertMarket(MarketSell market);

    MarketSell seleteMarket(Long id);

    List<MarketSellCom> selectMarketComments(Long id);

    MarketSellCom insertBuyComment(MarketSellCom comment);

    MarketSellCom findByIdMarketCom(Long commentId);

    MarketSellComRemover insertMarketComRemover(MarketSellComRemover marketComRemover);

    void updateMarketComRemover(Long id, MarketSellComRemover remover);

    MarketSell seleteAndViewCntMarket(Long id);

    void updateMarket(Long id, MarketSell market);

    void updateMarketForRemove(Long marketId, MarketSellRemover marketRemover);

    MarketSellRemover insertMarketRemover(MarketSellRemover marketRemover);

}
