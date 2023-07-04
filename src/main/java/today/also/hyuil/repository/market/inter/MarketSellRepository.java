package today.also.hyuil.repository.market.inter;

import today.also.hyuil.domain.market.MarketSell;
import today.also.hyuil.domain.market.MarketSellCom;
import today.also.hyuil.domain.market.MarketSellComRemover;
import today.also.hyuil.domain.market.MarketSellRemover;

import java.util.List;

public interface MarketSellRepository {

    List<MarketSellCom> selectMarketComments(Long id);

    MarketSellCom insertBuyComment(MarketSellCom comment);

    MarketSellCom findByIdMarketCom(Long commentId);

    MarketSellComRemover insertMarketComRemover(MarketSellComRemover marketComRemover);

    MarketSell seleteAndViewCntMarket(Long id);

    MarketSellRemover insertMarketRemover(MarketSellRemover marketRemover);

}
