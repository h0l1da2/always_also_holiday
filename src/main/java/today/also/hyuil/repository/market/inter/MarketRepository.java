package today.also.hyuil.repository.market.inter;

import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.domain.market.MarketComRemover;
import today.also.hyuil.domain.market.MarketRemover;

import java.util.List;

public interface MarketRepository {
    List<MarketCom> selectMarketComments(Long id);

    MarketCom insertBuyComment(MarketCom comment);

    MarketCom findByIdMarketCom(Long commentId);

    MarketComRemover insertMarketComRemover(MarketComRemover marketComRemover);

    Market seleteAndViewCntMarket(Long id);

    MarketRemover insertMarketRemover(MarketRemover marketRemover);
}
