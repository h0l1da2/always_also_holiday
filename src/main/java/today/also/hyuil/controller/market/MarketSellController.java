package today.also.hyuil.controller.market;

import com.google.gson.JsonObject;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.dto.fanLetter.*;
import today.also.hyuil.domain.dto.market.MarketViewDto;
import today.also.hyuil.domain.dto.market.MarketWriteDto;
import today.also.hyuil.domain.fanLetter.ReplyType;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.market.*;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.ThisEntityIsNull;
import today.also.hyuil.exception.fanLetter.MimeTypeNotMatchException;
import today.also.hyuil.service.market.inter.MarketSellService;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:application.yml")
@Controller
@RequestMapping("/market/sell")
public class MarketSellController {

    private final WebService webService;
    private final MarketSellService marketService;
    private final MemberJoinService memberJoinService;
    @Value("${file.fan.market.sell.path}")
    private String filePath;

    public MarketSellController(WebService webService, MarketSellService marketService, MemberJoinService memberJoinService) {
        this.webService = webService;
        this.marketService = marketService;
        this.memberJoinService = memberJoinService;
    }

    // TODO 마켓 판매 전체리스트 / 읽기O / 쓰기O / 수정 / 삭제 /
    // TODO 댓글 읽기 / 쓰기 / 삭제
    // TODO 뷰 페이지에 구매 버튼이 있고, 결제 페이지로 넘어갈 수 있게 구현 필요

    /**
     * 리스트 사이즈 = 6
     * 해당 파일까지 보여줘야 함
     */
    @GetMapping
    public String main(@PageableDefault(value = 6, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<MarketSell> marketList = marketService.listMain(pageable);

        /**
         * 1. 서버에서 필터링하는 법
         * 2. DB 에서 조건문을 준 후 가져오는 법
         * 뭐가 더 유리할까?
         */
        // 삭제 여부 필터링
        Page<BoardListDto> marketListDto = marketList
                        .map(market -> new BoardListDto(market));

        model.addAttribute("marketList", marketListDto);
        model.addAttribute("nowPage", pageable.getPageNumber());
        return "market/sell/sellList";
    }

    @GetMapping("/{id}")
    public String fanLetter(@PathVariable Long id, Model model, HttpServletRequest request) {
        // 글
        Map<String, Object> map = marketService.readSellPage(id);

        MarketSell market = (MarketSell) map.get("market");
        List<FileInfo> fileInfoList = (List<FileInfo>) map.get("fileInfoList");

        List<String> filePaths = webService.getFilePaths(fileInfoList);

        // 댓글 - Buy와 중복 코드인데... 어떡하지? 고민..
        List<MarketSellCom> commentList = marketService.readComments(id);
        List<CommentDto> comments = new ArrayList<>();

        for (MarketSellCom comment : commentList) {

            CommentDto commentDto = new CommentDto(comment);

            if (comment.getMarketSellComRemover() != null) {
                commentDto.itRemoved();
            }
            comments.add(commentDto);
        }

        // 이전글 다음글
        Map<String, MarketSell> prevNextMarket = marketService.prevNextMarket(id);

        MarketSell prevMarket = prevNextMarket.get("prev");
        MarketSell nextMarket = prevNextMarket.get("next");

        if (prevMarket != null) {
            model.addAttribute("prev", new PrevNextDto(prevMarket.getId(), prevMarket.getTitle()));
        }
        if (nextMarket != null) {
            model.addAttribute("next", new PrevNextDto(nextMarket.getId(), nextMarket.getTitle()));
        }

        model.addAttribute("market", new MarketViewDto(market));
        model.addAttribute("filePath", filePaths);
        model.addAttribute("comments", comments);

        // 본인 글인지 확인
        try {
            String nickname = webService.getNicknameInSession(request);
            if (nickname.equals(market.getMember().getNickname())) {
                model.addAttribute("nickname", nickname);
            }
        } catch (MemberNotFoundException e) {
            System.out.println("로그인이 안 되어 있음");
        }

        return "/market/sell/sellView";
    }


    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("sell", new MarketWriteDto());
        return "/market/sell/sellWrite";
    }

    @ResponseBody
    @PostMapping(value = "/write", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> write(@RequestPart(value = "image", required = false) List<MultipartFile> files,
                                        @RequestPart(value = "marketWriteDto") MarketWriteDto marketWriteDto,
                                        HttpServletRequest request) {

        Long marketId = null;
        try {
            // 세션에서 memberId 가져오기
//            Long id = webService.getIdInSession(request);
            Long id = 8L;

            if (!webService.marketWriteDtoNullCheck(marketWriteDto)) {
                System.out.println("글 내용이 없음");
                return new ResponseEntity<>("NOT_CONTENT", HttpStatus.BAD_REQUEST);
            }

            Md md = new Md(marketWriteDto);
            MarketSell market = new MarketSell(Status.SELL, marketWriteDto, md);


            // 이미지 파일이 존재할 경우
            // 여기에서 뭔가 문제가 발생
            List<FileInfo> fileInfoList = webService.getFileInfoList(files, filePath);

            MarketSell writSell = marketService.writeSell(id, market, fileInfoList);

            if (writSell.getId() == null) {
                System.out.println("작성 오류");
                return webService.badResponseEntity("WRITE_ERROR");
            }
            marketId = writSell.getId();

        } catch (MimeTypeNotMatchException e) {
            e.printStackTrace();
            System.out.println("이미지 파일 확장자 다름");
            return webService.badResponseEntity("MIMETYPE_ERROR");
//        } catch (MemberNotFoundException e) {
//            e.printStackTrace();
//            return webService.badResponseEntity("NOT_LOGIN");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 업로드 에러");
            return webService.badResponseEntity("FILE_UPLOAD_ERROR");
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", marketId);
        return webService.okResponseEntity(jsonObject);
    }

    @GetMapping("/modify/{id}")
    public String modifyBuy(@PathVariable Long id, Model model, HttpServletRequest request) {
        try {
            Long memberId = webService.getIdInSession(request);
//            Long memberId = 8L;

            MarketSell market = marketService.read(id);

            // 글 작성자와 로그인 한 사용자가 다를 경우
            if (!market.getMember().getId().equals(memberId))
                return "redirect:/loginForm?redirectUrl=/market/sell";

            model.addAttribute("market", new MarketViewDto(market));

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/market/sell";
        } catch (ThisEntityIsNull e) {
            e.printStackTrace();
            return "exception/notFound";
        }

        return "market/sell/sellModify";
    }

    @ResponseBody
    @PostMapping("/modify/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, HttpServletRequest request,
                                         @RequestBody MarketWriteDto marketWriteDto) {
        JsonObject jsonObject = new JsonObject();
        try {
            Long memberId = webService.getIdInSession(request);
//            Long memberId = 8L;

            if (!buyDtoNullCheck(marketWriteDto)) {
                System.out.println("글 내용이 없음");
                return webService.badResponseEntity("NOT_CONTENT");
            }

            MarketSell findMarket = marketService.read(id);

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

            MarketSell market = marketService.read(commentWriteDto.getBoardNum());
            MarketSellCom comment = getComment(commentWriteDto, member, market);

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

    private MarketSellCom getComment(CommentWriteDto commentWriteDto, Member member, MarketSell market) {
        MarketSellCom comment = new MarketSellCom();
        if (commentWriteDto.getCommentNum() == null) {
            comment.setCommentValues(member, ReplyType.COMMENT, commentWriteDto, market);
        } else {
            comment.setCommentValues(member, ReplyType.REPLY, commentWriteDto, market);
        }
        return comment;
    }

    private boolean buyDtoNullCheck(MarketWriteDto marketWriteDto) {
        if (marketWriteDto == null) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getTitle())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getName())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getContent())) {
            return false;
        }
        if (!StringUtils.hasText(marketWriteDto.getTrade().toString())) {
            return false;
        }
        if (marketWriteDto.getPrice() == null) {
            return false;
        }
        if (marketWriteDto.getQuantity() == null) {
            return false;
        }
        return true;
    }

}
