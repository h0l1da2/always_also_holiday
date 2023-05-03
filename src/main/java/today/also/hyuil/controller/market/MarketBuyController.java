package today.also.hyuil.controller.market;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // TODO 마켓 글쓰기
    @PostMapping("/write")
    public String write(@RequestBody BuyWriteDto buyWriteDto, HttpServletRequest request, Model model) {
        try {
            Long id = webService.getIdInSession(request);
            Member member = memberJoinService.findMyAccount(id);

            Md md = new Md(buyWriteDto);
            Market market = new Market(Status.BUY, buyWriteDto, member, md);

            Market writeMarket = marketService.writeBuy(market);

            MarketViewDto marketViewDto = new MarketViewDto(writeMarket);
            model.addAttribute("market", marketViewDto);

            // 이전글 , 다음글
            Map<String, Market> map = marketService.prevNextMarket(writeMarket.getId());
            Market prev = map.get("prev");
            Market next = map.get("next");

            model.addAttribute("prev", new PrevNextDto(prev.getId(), prev.getTitle()));
            model.addAttribute("next", new PrevNextDto(next.getId(), next.getTitle()));

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/market";
        }

        return "market/buyView";
    }

    private void forTestView(Model model) {
        List<BuyListDto> buyList = new ArrayList<>();
        BuyListDto buyListDto = new BuyListDto(1L, "title", "huil", new Date(), 1L);
        buyList.add(buyListDto);
        model.addAttribute("buyList", buyList);
    }
}
