package today.also.hyuil.service.market;

import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.market.*;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.repository.market.inter.MarketSellRepository;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.file.inter.FileService;
import today.also.hyuil.repository.market.inter.MarketSellJpaRepository;
import today.also.hyuil.service.market.inter.MarketSellService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class MarketSellServiceImpl implements MarketSellService {

    private final MarketSellRepository marketRepository;
    private final MarketSellJpaRepository marketJpaRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    public MarketSellServiceImpl(MarketSellRepository marketRepository, MarketSellJpaRepository marketJpaRepository, MemberRepository memberRepository, FileService fileService) {
        this.marketRepository = marketRepository;
        this.marketJpaRepository = marketJpaRepository;
        this.memberRepository = memberRepository;
        this.fileService = fileService;
    }

    @Override
    public MarketSell writeBuy(MarketSell market) {
        return marketRepository.insertMarket(market);
    }

    @Override
    public Map<String, MarketSell> prevNextMarket(Long id) {
        Long prev = id - 1;
        Long next = id + 1;
        MarketSell prevMarket = marketRepository.seleteMarket(prev);
        MarketSell nextMarket = marketRepository.seleteMarket(next);

        Map<String, MarketSell> map = new HashMap<>();
        map.put("prev", prevMarket);
        map.put("next", nextMarket);
        return map;
    }

    @Override
    public MarketSell read(Long id) throws ThisEntityIsNull {
        MarketSell market = marketRepository.seleteAndViewCntMarket(id);

        if (market == null) {
            throw new ThisEntityIsNull("해당 아이디의 구매글이 없습니다");
        }
        return market;
    }

    @Override
    public List<MarketSellCom> readComments(Long id) {
        return marketRepository.selectMarketComments(id);
    }

    @Override
    public MarketSellCom writeBuyComment(MarketSellCom comment) {
        return marketRepository.insertBuyComment(comment);
    }

    @Override
    public void removeBuyComment(Long commentId, Long memberId, Who who) throws NotFoundException, AccessDeniedException {
        MarketSellCom comment = marketRepository.findByIdMarketCom(commentId);

        if (comment == null) {
            throw new NotFoundException("해당 댓글을 찾을 수 없습니다");
        }

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("댓글을 쓴 본인만 삭제가 가능합니다");
        }

        MarketSellComRemover remover = marketRepository.insertMarketComRemover(
                new MarketSellComRemover(comment.getMember(), who));

        marketRepository.updateMarketComRemover(comment.getId(), remover);
    }

    @Override
    public Page<MarketSell> listMain(Pageable pageable) {
        // 현재페이지 / 페이지사이즈(10) / id 기준 오름차순
        Pageable pageRequest =
                PageRequest.of(pageable.getPageNumber(), 6, Sort.Direction.DESC, "id");

        return marketJpaRepository.findAll(pageRequest);
    }

    @Override
    public void modifyMarket(Long id, MarketSell market) {
        marketRepository.updateMarket(id, market);
    }

    @Override
    public void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException {
        MarketSell findMarket = marketRepository.seleteMarket(marketId);

        if (findMarket == null) {
            throw new NotFoundException("해당 글은 없는 글입니다");
        }
        Member findMember = memberRepository.findById(memberId);

        if (!findMember.getId().equals(findMarket.getMember().getId())) {
            throw new AccessDeniedException("본인 글만 삭제 가능합니다");
        }

        MarketSellRemover marketRemover = new MarketSellRemover(findMember, who);

        MarketSellRemover remover = marketRepository.insertMarketRemover(marketRemover);

        marketRepository.updateMarketForRemove(marketId, remover);
    }

    @Override
    public MarketSell writeSell(Long memberId, MarketSell market, List<FileInfo> fileInfoList) {
        Member member = memberRepository.findById(memberId);

        market.iAmSeller(member);
        MarketSell writeMarket = marketRepository.insertMarket(market);

        for (FileInfo fileInfo : fileInfoList) {
            fileInfo.marketSellFile(writeMarket);
            fileService.saveFileInfo(fileInfo);
        }

        return writeMarket;
    }

    @Override
    public Map<String, Object> readSellPage(Long id) {
        MarketSell market = marketRepository.seleteAndViewCntMarket(id);
        List<FileInfo> fileInfoList = fileService.fileInfoListForMarket(market.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("market", market);
        map.put("fileInfoList", fileInfoList);

        return map;
    }

}
