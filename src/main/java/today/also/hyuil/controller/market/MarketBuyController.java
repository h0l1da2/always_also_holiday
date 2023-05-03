package today.also.hyuil.controller.market;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import today.also.hyuil.domain.dto.market.BuyListDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/market/buy")
public class MarketBuyController {

    @GetMapping
    public String main(Model model) {
        // Test 용
        forTestView(model);


        return "/market/buyList";
    }

    private void forTestView(Model model) {
        List<BuyListDto> buyList = new ArrayList<>();
        BuyListDto buyListDto = new BuyListDto(1L, "title", "huil", new Date(), 1L);
        buyList.add(buyListDto);
        model.addAttribute("buyList", buyList);
    }
}
