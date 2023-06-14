package today.also.hyuil.service.market.inter;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.market.MarketSell;
import today.also.hyuil.domain.market.MarketSellCom;
import today.also.hyuil.exception.ThisEntityIsNull;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

public interface MarketSellService {
    MarketSell writeBuy(MarketSell market);
    Map<String, MarketSell> prevNextMarket(Long id);
    MarketSell read(Long id) throws ThisEntityIsNull;
    List<MarketSellCom> readComments(Long id);
    MarketSellCom writeBuyComment(MarketSellCom comment);
    void removeBuyComment(Long commentId, Long memberId, Who who) throws NotFoundException, AccessDeniedException;
    Page<MarketSell> listMain(Pageable pageable);
    void modifyMarket(Long id, MarketSell market);
    void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException;
    MarketSell writeSell(Long memberId, MarketSell market, List<FileInfo> fileInfoList);
    Map<String, Object> readSellPage(Long id);

}
