package today.also.hyuil.service.market;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketComBuy;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.repository.market.MarketRepository;
import today.also.hyuil.service.market.inter.MarketService;

import java.util.HashMap;
import java.util.List;
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

    @Override
    public Market read(Long id) throws ThisEntityIsNull {
        Market market = marketRepository.seleteMarket(id);

        if (market == null) {
            throw new ThisEntityIsNull("해당 아이디의 구매글이 없습니다");
        }
        return market;
    }

    @Override
    public List<MarketComBuy> readBuyComment(Long id) {
        return marketRepository.selectMarketBuyComments(id);
    }

    @Override
    public MarketComBuy writeBuyComment(MarketComBuy comment) {
        return marketRepository.insertBuyComment(comment);
    }
}
