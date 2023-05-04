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
import today.also.hyuil.domain.market.MarketRemover;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.repository.market.MarketJpaRepository;
import today.also.hyuil.repository.market.MarketRepository;
import today.also.hyuil.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;

    public MarketServiceImpl(MarketRepository marketRepository, MarketJpaRepository marketJpaRepository, MemberRepository memberRepository) {
        this.marketRepository = marketRepository;
        this.marketJpaRepository = marketJpaRepository;
        this.memberRepository = memberRepository;
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

    @Override
    public void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException {
        Market findMarket = marketRepository.seleteMarket(marketId);

        if (findMarket == null) {
            throw new NotFoundException("해당 글은 없는 글입니다");
        }
        Member findMember = memberRepository.findById(memberId);

        if (!findMember.getId().equals(findMarket.getMember().getId())) {
            throw new AccessDeniedException("본인 글만 삭제 가능합니다");
        }

        MarketRemover marketRemover = new MarketRemover(findMember, who);

        MarketRemover remover = marketRepository.insertMarketRemover(marketRemover);

        marketRepository.updateMarketForRemove(marketId, remover);
    }

}
