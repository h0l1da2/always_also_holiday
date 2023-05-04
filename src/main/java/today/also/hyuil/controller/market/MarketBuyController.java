package today.also.hyuil.controller.market;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.fanLetter.CommentDto;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.dto.fanLetter.PrevNextDto;
import today.also.hyuil.domain.dto.market.MarketViewDto;
import today.also.hyuil.domain.dto.market.buy.BuyListDto;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;
import today.also.hyuil.domain.fanLetter.Comment;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.MarketCom;
import today.also.hyuil.domain.market.Md;
import today.also.hyuil.domain.market.Status;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.service.market.inter.MarketService;
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

            Md md = new Md(buyWriteDto);
            Market market = new Market(Status.BUY, buyWriteDto, member, md);

            writeMarket = marketService.writeBuy(market);

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

    @GetMapping("/{id}")
    public String read(@PathVariable Long id, Model model) {

        try {
            // 글 내용
            Market market = marketService.read(id);

            MarketViewDto marketViewDto = new MarketViewDto(market);
            model.addAttribute("market", marketViewDto);

            // 이전글 , 다음글
            Map<String, Market> map = marketService.prevNextMarket(market.getId());
            Market prev = map.get("prev");
            Market next = map.get("next");

            if (prev != null) {
                model.addAttribute("prev", new PrevNextDto(prev.getId(), prev.getTitle()));
            }
            if (next != null) {
                model.addAttribute("next", new PrevNextDto(next.getId(), next.getTitle()));
            }

            // 댓글
            List<MarketCom> commentList = marketService.readComment(id);
            List<CommentDto> comments = new ArrayList<>();

            for (MarketCom comment : commentList) {

                CommentDto commentDto = new CommentDto(comment);

                if (comment.getCommentRemover() != null) {
                    commentDto.itRemoved();
                }
                comments.add(commentDto);
            }

        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return "exception/notFound";
        }


        return "market/buyView";
    }

    @PostMapping("/comment/write")
    public ResponseEntity<String> write(@RequestBody CommentWriteDto commentWriteDto, HttpServletRequest request) {

        System.out.println("fanCommentWriteDto = " + commentWriteDto);
        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */
        JsonObject jsonObject = new JsonObject();

        try {

            if (!writeDtoNullCheck(commentWriteDto)) {
                System.out.println("comment NULL 들어옴");
                return badResponseEntity("COMMENT_NULL");
            }
            Long id = webService.getIdInSession(request);
//            String memberId = "aaaa1";

            Member member = memberJoinService.findMyAccount(id);

            if (member == null) {
                return badResponseEntity("MEMBER_NOT_FOUND");
            }

            Map<String, Object> map = fanLetterService.readLetter(commentWriteDto.getLetterNum());
            FanBoard fanBoard = (FanBoard) map.get("fanLetter");
            Comment comment = getComment(commentWriteDto, member, fanBoard);

            // 작성 완료
            fanLetterCommentService.writeComment(comment);
            jsonObject.addProperty("data", "WRITE_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return badResponseEntity("MEMBER_NOT_FOUND");
        }
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(jsonResponse);
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
