package today.also.hyuil.market.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.member.domain.type.Who;
import today.also.hyuil.fanletter.dto.BoardListDto;
import today.also.hyuil.fanletter.dto.CommentDto;
import today.also.hyuil.fanletter.dto.CommentWriteDto;
import today.also.hyuil.fanletter.dto.PrevNextDto;
import today.also.hyuil.market.dto.MarketViewDto;
import today.also.hyuil.market.dto.MarketWriteDto;
import today.also.hyuil.fanletter.domain.ReplyType;
import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketCom;
import today.also.hyuil.market.domain.Md;
import today.also.hyuil.market.domain.Status;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.MemberNotFoundException;
import today.also.hyuil.common.exception.ThisEntityIsNull;
import today.also.hyuil.market.service.MarketService;
import today.also.hyuil.member.service.MemberJoinService;
import today.also.hyuil.common.web.WebService;

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

        /**
         * 1. 서버에서 필터링하는 법
         * 2. DB 에서 조건문을 준 후 가져오는 법
         * 뭐가 더 유리할까?
         */
        // 삭제 여부 필터링
        List<BoardListDto> marketListDto =
                marketList
                .filter(market -> market.getMarketRemover() == null)
                .map(market -> new BoardListDto(market))
                .toList();

        // 페이지로 캐스팅
        Page<BoardListDto> page = webService.listToPage(pageable, marketListDto);

        model.addAttribute("marketList", page);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "market/buy/buyList";
    }

    @GetMapping("/{id}")
    public String read(@PathVariable Long id, Model model, HttpServletRequest request) {

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
            List<MarketCom> commentList = marketService.readComments(id);
            List<CommentDto> comments = new ArrayList<>();


            for (MarketCom comment : commentList) {

                CommentDto commentDto = new CommentDto(comment);

                if (comment.getMarketComRemover() != null) {
                    commentDto.itRemoved();
                }
                comments.add(commentDto);
            }

            model.addAttribute("comments", comments);

            // 본인 글인지 확인
            String nickname = webService.getNicknameInSession(request);
            if (nickname.equals(market.getMember().getNickname())) {
                model.addAttribute("nickname", nickname);
            }
        } catch (MemberNotFoundException e) {
            System.out.println("로그인이 안 되어 있음");
        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return "exception/notFound";
        }


        return "market/buy/buyView";
    }


    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("buy", new MarketWriteDto());
        return "/market/buy/buyWrite";
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<String> write(@RequestBody MarketWriteDto marketWriteDto, HttpServletRequest request, Model model) {

        System.out.println("buyWriteDto = " + marketWriteDto);
        Market writeMarket = new Market();
        try {
            Long id = webService.getIdInSession(request);
            Member member = memberJoinService.findMyAccount(id);

            if (!webService.marketWriteDtoNullCheck(marketWriteDto)) {
                return ResponseEntity.badRequest()
                        .body("DTO_NULL");
            }

            if (marketWriteDto.getPrice() < 100) {
                return ResponseEntity.badRequest()
                        .body("MINIMUM_PRICE");
            }

            if (marketWriteDto.getQuantity() < 1) {
                return ResponseEntity.badRequest()
                        .body("MINIMUM_QUANTITY");
            }

            Md md = new Md(marketWriteDto);
            Market market = new Market(Status.BUY, marketWriteDto, member, md);

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
    public String modifyBuy(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            Long memberId = webService.getIdInSession(request);
//            Long memberId = 8L;

            Market market = marketService.read(id);

            // 글 작성자와 로그인 한 사용자가 다를 경우
            if (!market.getMember().getId().equals(memberId))
                return "redirect:/loginForm?redirectUrl=/market/buy";

            model.addAttribute("market", new MarketViewDto(market));

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/market/buy";
        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return "exception/notFound";
        }

        return "market/buy/buyModify";
    }

    @ResponseBody
    @PostMapping("/modify/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, HttpServletRequest request,
                                         @RequestBody MarketWriteDto marketWriteDto) {
        JsonObject jsonObject = new JsonObject();
        try {
            Long memberId = webService.getIdInSession(request);
//            Long memberId = 8L;

            if (!webService.marketWriteDtoNullCheck(marketWriteDto)) {
                System.out.println("글 내용이 없음");
                return webService.badResponseEntity("NOT_CONTENT");
            }

            Market findMarket = marketService.read(id);

            if (findMarket == null) {
                System.out.println("해당 글은 존재하지 않음");
                return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }

            if (!findMarket.getMember().getId().equals(memberId)) {
                System.out.println("본인 글이 아님");
                return webService.badResponseEntity("NOT_ACCESS");
            }

            findMarket.changeToBuyWriteDto(marketWriteDto);

            marketService.modifyMarket(id, findMarket);
            jsonObject.addProperty("data", "MODIFY_OK");

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_LOGIN");
        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_FOUND");
        }

        return webService.okResponseEntity(jsonObject);
    }

    @PostMapping("/remove/{id}")
    public ResponseEntity<String> deleteBuy(@PathVariable Long id, HttpServletRequest request) {

        try {
//            Long memberId = webService.getIdInSession(request);
            Long memberId = 8L;

            marketService.removeMarket(id, Who.MEMBER, memberId);

//        } catch (MemberNotFoundException e) {
//            e.printStackTrace();
//            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_YOUR_MARKET");
        } catch (NotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_FOUND");
        }

        return ResponseEntity.ok()
                .body("REMOVE_OK");

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


}
