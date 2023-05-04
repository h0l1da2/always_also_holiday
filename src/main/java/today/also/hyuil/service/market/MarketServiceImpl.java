package today.also.hyuil.service.market;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.market.MarketComRemover;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.domain.market.Md;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.repository.market.MarketJpaRepository;
import today.also.hyuil.repository.market.MarketRepository;
import today.also.hyuil.service.market.inter.MarketService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketJpaRepository marketJpaRepository;

    public MarketServiceImpl(MarketRepository marketRepository, MarketJpaRepository marketJpaRepository) {
        this.marketRepository = marketRepository;
        this.marketJpaRepository = marketJpaRepository;
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
        Market market = marketRepository.seleteAndViewCntMarket(id);

        if (market == null) {
            throw new ThisEntityIsNull("해당 아이디의 구매글이 없습니다");
        }
        return market;
    }

    @Override
    public List<MarketCom> readBuyComment(Long id) {
        return marketRepository.selectMarketBuyComments(id);
    }

    @Override
    public MarketCom writeBuyComment(MarketCom comment) {
        return marketRepository.insertBuyComment(comment);
    }

    @Override
    public void removeBuyComment(Long commentId, Long memberId, Who who) throws NotFoundException, AccessDeniedException {
        MarketCom comment = marketRepository.findByIdMarketCom(commentId);

        if (comment == null) {
            throw new NotFoundException("해당 댓글을 찾을 수 없습니다");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("댓글을 쓴 본인만 삭제가 가능합니다");
        }

        MarketComRemover remover = marketRepository.insertMarketComRemover(
                new MarketComRemover(comment.getMember(), who));

        marketRepository.updateMarketComRemover(comment.getId(), remover);
    }

    @Override
    public Page<Market> listMain(Pageable pageable) {
        // 현재페이지 / 페이지사이즈(10) / id 기준 오름차순
        Pageable pageRequest =
                PageRequest.of(pageable.getPageNumber(), 10, Sort.Direction.DESC, "id");

        return marketJpaRepository.findAll(pageRequest);
    }

    @Override
    public void modifyMarket(Long id, Market market) {
        marketRepository.updateMarket(id, market);
    }

}
