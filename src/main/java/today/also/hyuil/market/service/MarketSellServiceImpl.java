package today.also.hyuil.market.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.member.domain.type.Who;
import today.also.hyuil.file.domain.FileInfo;
import today.also.hyuil.market.domain.MarketSell;
import today.also.hyuil.market.domain.MarketSellCom;
import today.also.hyuil.market.domain.MarketSellComRemover;
import today.also.hyuil.market.domain.MarketSellRemover;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.ThisEntityIsNull;
import today.also.hyuil.market.repository.MarketSellJpaRepository;
import today.also.hyuil.market.repository.MarketSellRepository;
import today.also.hyuil.file.service.FileService;
import today.also.hyuil.member.service.MemberJoinService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketSellServiceImpl implements MarketSellService {

    private final MarketSellRepository marketRepository;
    private final MarketSellJpaRepository marketJpaRepository;
    private final MemberJoinService memberJoinService;
    private final FileService fileService;

    @Override
    public MarketSell writeBuy(MarketSell market) {
        return marketJpaRepository.save(market);
    }

    @Override
    public Map<String, MarketSell> prevNextMarket(Long id) {
        Long prev = id - 1;
        Long next = id + 1;
        MarketSell prevMarket = marketJpaRepository.findById(prev).orElse(null);
        MarketSell nextMarket = marketJpaRepository.findById(next).orElse(null);

        Map<String, MarketSell> map = new HashMap<>();
        map.put("prev", prevMarket);
        map.put("next", nextMarket);
        return map;
    }

    @Transactional
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

    @Transactional
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

        comment.itRemove(remover);
        marketRepository.insertBuyComment(comment);
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
        marketJpaRepository.save(market);
    }

    @Transactional
    @Override
    public void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException {
        MarketSell findMarket = marketJpaRepository.findById(marketId).orElse(null);

        if (findMarket == null) {
            throw new NotFoundException("해당 글은 없는 글입니다");
        }
        Member findMember = memberJoinService.findMyAccount(memberId);

        if (!findMember.getId().equals(findMarket.getMember().getId())) {
            throw new AccessDeniedException("본인 글만 삭제 가능합니다");
        }

        MarketSellRemover marketRemover = new MarketSellRemover(findMember, who);

        MarketSellRemover remover = marketRepository.insertMarketRemover(marketRemover);
        findMarket.itRemove(remover);
        marketJpaRepository.save(findMarket);
    }

    @Transactional
    @Override
    public MarketSell writeSell(Long memberId, MarketSell market, List<FileInfo> fileInfoList) {
        Member member = memberJoinService.findMyAccount(memberId);

        market.iAmSeller(member);
        MarketSell writeMarket = marketJpaRepository.save(market);

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
