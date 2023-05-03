package today.also.hyuil.controller.market;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import today.also.hyuil.domain.dto.market.buy.BuyListDto;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;
import today.also.hyuil.service.web.WebService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/market/buy")
public class MarketBuyController {

    private final WebService webService;

    public MarketBuyController(WebService webService) {
        this.webService = webService;
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

    private void forTestView(Model model) {
        List<BuyListDto> buyList = new ArrayList<>();
        BuyListDto buyListDto = new BuyListDto(1L, "title", "huil", new Date(), 1L);
        buyList.add(buyListDto);
        model.addAttribute("buyList", buyList);
    }
}
