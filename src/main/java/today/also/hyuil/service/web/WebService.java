package today.also.hyuil.service.web;

import org.springframework.stereotype.Service;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

        } catch (NullPointerException e) {
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

}
