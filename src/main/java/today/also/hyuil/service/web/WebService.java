package today.also.hyuil.service.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import today.also.hyuil.domain.dto.fanLetter.BoardListDto;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.dto.market.MarketWriteDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;

import java.util.List;

@Service
public class WebService {

    public void sessionSetMember(Member member, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("id", member.getId());
        session.setAttribute("nickname", member.getNickname());
    }

    public Long getIdInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        Long id = null;
        try {
            String stringId = String.valueOf(session.getAttribute("id"));
            id = Long.parseLong(stringId);

        } catch (NumberFormatException e) {
            throw new MemberNotFoundException("세션에 아이디가 없음");
        }
        return id;
    }
    public String getNicknameInSession(HttpServletRequest request) throws MemberNotFoundException {
        HttpSession session = request.getSession();
        String nickname = null;
        try {
            nickname = String.valueOf(session.getAttribute("nickname"));
            System.out.println("nickname = " + nickname);
        } catch (NullPointerException e) {
            throw new MemberNotFoundException("세션에 닉네임이 없음");
        }
        return nickname;
    }

    public ResponseEntity<String> okResponseEntity(JsonObject jsonObject) {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    public ResponseEntity<String> badResponseEntity(String cause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", cause);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.badRequest()
                .body(jsonResponse);
    }

    public Page<BoardListDto> listToPage(Pageable pageable, List dtoList) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()),  (int) pageable.getOffset() + dtoList.size());
        return new PageImpl<>(dtoList.subList(start, end), pageable, dtoList.size());
    }

    public boolean commentWriteDtoNullCheck(CommentWriteDto commentWriteDto) {
        if (commentWriteDto == null) {
            return false;
        }
        if (commentWriteDto.getBoardNum() == null) {
            return false;
        }
        if (!StringUtils.hasText(commentWriteDto.getContent())) {
            return false;
        }
        return true;
    }

    public boolean marketWriteDtoNullCheck(MarketWriteDto marketWriteDto) {
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


    public ResponseEntity<String> okResponse(JsonObject jsonObject) {
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(jsonObject);
        return ResponseEntity.ok()
                .body(jsonResponse);
    }
}
