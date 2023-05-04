package today.also.hyuil.service.market.inter;

import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.exception.ThisEntityIsNull;

import java.util.List;
import java.util.Map;

public interface MarketService {
    Market writeBuy(Market market);
    Map<String, Market> prevNextMarket(Long id);
    Market read(Long id) throws ThisEntityIsNull;
    List<MarketCom> readComment(Long id);
    MarketCom writeComment(MarketCom comment);
}
