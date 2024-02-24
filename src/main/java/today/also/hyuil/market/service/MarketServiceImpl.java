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
import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketCom;
import today.also.hyuil.market.domain.MarketComRemover;
import today.also.hyuil.market.domain.MarketRemover;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.ThisEntityIsNull;
import today.also.hyuil.market.repository.MarketJpaRepository;
import today.also.hyuil.market.repository.MarketRepository;
import today.also.hyuil.file.service.FileService;
import today.also.hyuil.member.service.MemberJoinService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final MarketRepository marketRepository;
    private final MarketJpaRepository marketJpaRepository;
    private final MemberJoinService memberJoinService;
    private final FileService fileService;

    @Override
    public Market writeBuy(Market market) {
        return marketJpaRepository.save(market);
    }

    @Override
    public Map<String, Market> prevNextMarket(Long id) {
        Long prev = id - 1;
        Long next = id + 1;
        Market prevMarket = marketJpaRepository.findById(prev).orElse(null);
        Market nextMarket = marketJpaRepository.findById(next).orElse(null);

        Map<String, Market> map = new HashMap<>();
        map.put("prev", prevMarket);
        map.put("next", nextMarket);
        return map;
    }

    @Transactional
    @Override
    public Market read(Long id) throws ThisEntityIsNull {
        Market market = marketRepository.seleteAndViewCntMarket(id);

        if (market == null) {
            throw new ThisEntityIsNull("해당 아이디의 구매글이 없습니다");
        }
        return market;
    }

    @Override
    public List<MarketCom> readComments(Long id) {
        return marketRepository.selectMarketComments(id);
    }

    @Override
    public MarketCom writeBuyComment(MarketCom comment) {
        return marketRepository.insertBuyComment(comment);
    }

    @Transactional
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

        comment.itRemove(remover);
        marketRepository.insertBuyComment(comment);
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
        marketJpaRepository.save(market);
    }

    @Transactional
    @Override
    public void removeMarket(Long marketId, Who who, Long memberId) throws NotFoundException, AccessDeniedException {
        Market findMarket = marketJpaRepository.findById(marketId).orElse(null);

        if (findMarket == null) {
            throw new NotFoundException("해당 글은 없는 글입니다");
        }
        Member findMember = memberJoinService.findMyAccount(memberId);

        if (!findMember.getId().equals(findMarket.getMember().getId())) {
            throw new AccessDeniedException("본인 글만 삭제 가능합니다");
        }

        MarketRemover marketRemover = new MarketRemover(findMember, who);

        MarketRemover remover = marketRepository.insertMarketRemover(marketRemover);

        findMarket.itRemove(remover);
        marketJpaRepository.save(findMarket);
    }

    @Transactional
    @Override
    public Market writeSell(Long memberId, Market market, List<FileInfo> fileInfoList) {
        Member member = memberJoinService.findMyAccount(memberId);

        market.iAmSeller(member);
        Market writeMarket = marketJpaRepository.save(market);

        for (FileInfo fileInfo : fileInfoList) {
            fileInfo.marketFile(writeMarket);
            fileService.saveFileInfo(fileInfo);
        }

        return writeMarket;
    }

    @Override
    public Map<String, Object> readSellPage(Long id) {
        Market market = marketRepository.seleteAndViewCntMarket(id);
        List<FileInfo> fileInfoList = fileService.fileInfoListForMarket(market.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("market", market);
        map.put("fileInfoList", fileInfoList);

        return map;
    }

}
