package today.also.hyuil.service.market;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.repository.market.MarketRepository;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;

    public MarketServiceImpl(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    @Override
    public Market writeBuy(Market market) {
        return marketRepository.insertMarket(market);
    }

    @Override
    public Map<String, Market> prevNextMarket(Long id) {
        Long prev = id - 1;
        Long next = id + 1;
        Market prevMarket = marketRepository.seleteMarket(prev);
        Market nextMarket = marketRepository.seleteMarket(next);

        Map<String, Market> map = new HashMap<>();
        map.put("prev", prevMarket);
        map.put("next", nextMarket);
        return map;
    }
}
