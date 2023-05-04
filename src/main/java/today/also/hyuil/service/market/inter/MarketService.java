package today.also.hyuil.service.market.inter;

import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketComBuy;
import today.also.hyuil.exception.ThisEntityIsNull;

import java.util.List;
import java.util.Map;

public interface MarketService {
    Market writeBuy(Market market);
    Map<String, Market> prevNextMarket(Long id);
    Market read(Long id) throws ThisEntityIsNull;
    List<MarketComBuy> readBuyComment(Long id);
    MarketComBuy writeBuyComment(MarketComBuy comment);
}
