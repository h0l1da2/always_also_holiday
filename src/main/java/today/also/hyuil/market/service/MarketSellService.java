package today.also.hyuil.market.service;

import com.amazonaws.services.kms.model.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import today.also.hyuil.member.domain.type.Who;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.market.domain.MarketSell;
import today.also.hyuil.market.domain.MarketSellCom;
import today.also.hyuil.common.exception.ThisEntityIsNull;

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
