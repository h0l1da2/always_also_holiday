package today.also.hyuil.controller.market;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.fanLetter.PrevNextDto;
import today.also.hyuil.domain.dto.market.MarketViewDto;
import today.also.hyuil.domain.dto.market.buy.BuyListDto;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.Md;
import today.also.hyuil.domain.market.Status;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.service.market.MarketService;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/market/buy")
public class MarketBuyController {

    private final WebService webService;
    private final MarketService marketService;
    private final MemberJoinService memberJoinService;

    public MarketBuyController(WebService webService, MarketService marketService, MemberJoinService memberJoinService) {
        this.webService = webService;
        this.marketService = marketService;
        this.memberJoinService = memberJoinService;
    }

    @GetMapping
    public String main(Model model) {
        // Test 용
        forTestView(model);
        return "/market/buyList";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("buy", new BuyWriteDto());
        return "/market/buyWrite";
    }

    // TODO 수량, 가격 검증 로직 추가 필요 (널, 공백 체크)
    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody BuyWriteDto buyWriteDto, HttpServletRequest request, Model model) {

        System.out.println("buyWriteDto = " + buyWriteDto);
        Market writeMarket = new Market();
        try {
            Long id = webService.getIdInSession(request);
            Member member = memberJoinService.findMyAccount(id);

            if (!buyDtoNullCheck(buyWriteDto)) {
                return ResponseEntity.badRequest()
                        .body("DTO_NULL");
            }

            if (buyWriteDto.getPrice() < 100) {
                return ResponseEntity.badRequest()
                        .body("MINIMUM_PRICE");
            }

            if (buyWriteDto.getQuantity() < 1) {
                return ResponseEntity.badRequest()
                        .body("MINIMUM_QUANTITY");
            }

//            Member member = memberJoinService.findMyAccount(8L);

            Md md = new Md(buyWriteDto);
            Market market = new Market(Status.BUY, buyWriteDto, member, md);

            writeMarket = marketService.writeBuy(market);

            MarketViewDto marketViewDto = new MarketViewDto(writeMarket);
            model.addAttribute("market", marketViewDto);

            // 이전글 , 다음글
            Map<String, Market> map = marketService.prevNextMarket(writeMarket.getId());
            Market prev = map.get("prev");
            Market next = map.get("next");

            if (prev != null) {
                model.addAttribute("prev", new PrevNextDto(prev.getId(), prev.getTitle()));
            }
            if (next != null) {
                model.addAttribute("next", new PrevNextDto(next.getId(), next.getTitle()));
            }

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("MEMBER_NOT_LOGIN");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("ONLY_NUMBER");
        }

        return ResponseEntity.ok()
                .body(writeMarket.getId().toString());
    }

    private void forTestView(Model model) {
        List<BuyListDto> buyList = new ArrayList<>();
        BuyListDto buyListDto = new BuyListDto(1L, "title", "huil", new Date(), 1L);
        buyList.add(buyListDto);
        model.addAttribute("buyList", buyList);
    }

    private boolean buyDtoNullCheck(BuyWriteDto buyWriteDto) {
        if (buyWriteDto == null) {
            return false;
        }
        if (!StringUtils.hasText(buyWriteDto.getTitle())) {
            return false;
        }
        if (!StringUtils.hasText(buyWriteDto.getName())) {
            return false;
        }
        if (!StringUtils.hasText(buyWriteDto.getContent())) {
            return false;
        }
        if (!StringUtils.hasText(buyWriteDto.getTrade().toString())) {
            return false;
        }
        if (buyWriteDto.getPrice() == null) {
            return false;
        }
        if (buyWriteDto.getQuantity() == null) {
            return false;
        }
        return true;
    }
}
