package today.also.hyuil.controller.market;

import com.google.gson.JsonObject;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.dto.fanLetter.BoardListDto;
import today.also.hyuil.domain.dto.fanLetter.CommentDto;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.dto.fanLetter.PrevNextDto;
import today.also.hyuil.domain.dto.market.MarketViewDto;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;
import today.also.hyuil.domain.fanLetter.ReplyType;
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
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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
    public String main(@PageableDefault Pageable pageable, Model model) {

        Page<Market> marketList = marketService.listMain(pageable);
        Page<BoardListDto> marketListDto =
                marketList.map(market -> new BoardListDto(market));

        model.addAttribute("marketList", marketListDto);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "market/buy/buyList";
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
            List<MarketCom> commentList = marketService.readBuyComment(id);
            List<CommentDto> comments = new ArrayList<>();


            for (MarketCom comment : commentList) {

                CommentDto commentDto = new CommentDto(comment);

                if (comment.getMarketComRemover() != null) {
                    commentDto.itRemoved();
                }
                comments.add(commentDto);
            }

            model.addAttribute("comments", comments);

        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return "exception/notFound";
        }


        return "market/buy/buyView";
    }


    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("buy", new BuyWriteDto());
        return "/market/buy/buyWrite";
    }

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

    @GetMapping("/modify/{id}")
    public String modifyBuy(@PathVariable Long id) {

        return "market/buy/modifyBuy";
    }

    @ResponseBody
    @PostMapping("/comment/remove")
    public ResponseEntity<String> remove(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            Long memberId = webService.getIdInSession(request);

            marketService.removeBuyComment(commentDto.getId(), memberId, Who.MEMBER);
            jsonObject.addProperty("data", "REMOVE_OK");
        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("COMMENT_NOT_FOUND");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("NOT_YOUR_COMMENT");
        } catch (NumberFormatException e) {
            System.out.println("타입 변환 에러(코멘트 파라미터가 숫자 타입이 아님)");
            return ResponseEntity.badRequest()
                    .body("BAD_COMMENT_ID");
        }

        return webService.okResponseEntity(jsonObject);
    }

    @ResponseBody
    @PostMapping("/comment/write")
    public ResponseEntity<String> write(@RequestBody CommentWriteDto commentWriteDto, HttpServletRequest request) {

        System.out.println("CommentWriteDto = " + commentWriteDto);
        /**
         * 해당 글 번호, 부모 댓글 번호, 본인 아이디, 내용...
         */
        JsonObject jsonObject = new JsonObject();

        try {

            if (!webService.commentWriteDtoNullCheck(commentWriteDto)) {
                System.out.println("comment NULL 들어옴");
                return webService.badResponseEntity("COMMENT_NULL");
            }
            Long id = webService.getIdInSession(request);
//            Long id = 8L;

            Member member = memberJoinService.findMyAccount(id);

            if (member == null) {
                return webService.badResponseEntity("MEMBER_NOT_FOUND");
            }

            Market market = marketService.read(commentWriteDto.getBoardNum());
            MarketCom comment = getComment(commentWriteDto, member, market);

            // 작성 완료
            marketService.writeBuyComment(comment);
            jsonObject.addProperty("data", "WRITE_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return webService.badResponseEntity("MARKET_NOT_FOUND");
        }
        return webService.okResponseEntity(jsonObject);
    }

    private MarketCom getComment(CommentWriteDto commentWriteDto, Member member, Market market) {
        MarketCom comment = new MarketCom();
        if (commentWriteDto.getCommentNum() == null) {
            comment.setCommentValues(member, ReplyType.COMMENT, commentWriteDto, market);
        } else {
            comment.setCommentValues(member, ReplyType.REPLY, commentWriteDto, market);
        }
        return comment;
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
