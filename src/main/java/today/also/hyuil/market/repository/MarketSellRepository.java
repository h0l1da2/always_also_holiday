package today.also.hyuil.market.repository;

import today.also.hyuil.market.domain.MarketSell;
import today.also.hyuil.market.domain.MarketSellCom;
import today.also.hyuil.market.domain.MarketSellComRemover;
import today.also.hyuil.market.domain.MarketSellRemover;

import java.util.List;

public interface MarketSellRepository {

    List<MarketSellCom> selectMarketComments(Long id);

    MarketSellCom insertBuyComment(MarketSellCom comment);

    MarketSellCom findByIdMarketCom(Long commentId);

    MarketSellComRemover insertMarketComRemover(MarketSellComRemover marketComRemover);

    MarketSell seleteAndViewCntMarket(Long id);

    MarketSellRemover insertMarketRemover(MarketSellRemover marketRemover);

}
