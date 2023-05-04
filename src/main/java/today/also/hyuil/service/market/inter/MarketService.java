package today.also.hyuil.service.market.inter;

import javassist.NotFoundException;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.exception.ThisEntityIsNull;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface MarketService {
    Market writeBuy(Market market);
    Map<String, Market> prevNextMarket(Long id);
    Market read(Long id) throws ThisEntityIsNull;
    List<MarketCom> readBuyComment(Long id);
    MarketCom writeBuyComment(MarketCom comment);
    void removeBuyComment(Long commentId, Long memberId, Who who) throws NotFoundException, AccessDeniedException;
}
