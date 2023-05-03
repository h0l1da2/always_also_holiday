package today.also.hyuil.service.market;

import today.also.hyuil.domain.market.Market;

import java.util.Map;

public interface MarketService {
    Market writeBuy(Market market);
    Map<String, Market> prevNextMarket(Long id);
}
