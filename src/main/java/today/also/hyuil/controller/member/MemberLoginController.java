package today.also.hyuil.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberLoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    public MemberLoginController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @GetMapping("/loginForm")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginDto());
        return "member/loginForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request, HttpServletResponse response, Model model) {
        Member member = new Member(loginDto);
        boolean idPwdValid = memberLoginService.idPwdValid(member);

        // idPwd 틀리면 오류폼
        if (!idPwdValid) {
            return "redirect:/loginForm";
        }

        Map<String, String> tokens = memberLoginService.getTokens(member);
        String refreshToken = tokens.get("refreshToken");
        String accessToken = tokens.get("accessToken");

        // 각 토큰 저장
        memberLoginService.saveRefreshToken(member.getMemberId(), refreshToken);
        response.setHeader("Authorization", accessToken);

        /**
         * 자동로그인기능 쿠키생성(나중에)
         */

        if (request.getRequestURI() != null) {
            return "redirect:/"+request.getRequestURI();
        }
        return "index";
    }
}
