package today.also.hyuil.market.repository;

import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketCom;
import today.also.hyuil.market.domain.MarketComRemover;
import today.also.hyuil.market.domain.MarketRemover;

import java.util.List;

public interface MarketRepository {
    List<MarketCom> selectMarketComments(Long id);

    MarketCom insertBuyComment(MarketCom comment);

    MarketCom findByIdMarketCom(Long commentId);

    MarketComRemover insertMarketComRemover(MarketComRemover marketComRemover);

    Market seleteAndViewCntMarket(Long id);

    MarketRemover insertMarketRemover(MarketRemover marketRemover);
}
