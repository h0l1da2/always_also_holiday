package today.also.hyuil.service.market.inter;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.file.FileInfo;
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
    List<MarketCom> readComments(Long id);
    MarketCom writeBuyComment(MarketCom comment);
    void removeBuyComment(Long commentId, Long memberId, Who who) throws NotFoundException, AccessDeniedException;
    Page<Market> listMain(Pageable pageable);
    void modifyMarket(Long id, Market market);
    void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException;
    Market writeSell(Long memberId, Market market, List<FileInfo> fileInfoList);
    Map<String, Object> readSellPage(Long id);
}
