package today.also.hyuil.controller.member;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberLoginService;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberLoginController {

    private final MemberLoginService memberLoginService;

    public MemberLoginController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @GetMapping("/loginForm")
    public String loginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("loginForm", new LoginDto());
        if (error != null && error.equals("error")) {
            System.out.println("error = " + error);
            model.addAttribute(error, "아이디 또는 비밀번호가 틀립니다");
        }
        return "member/loginForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public Map loginToken(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        Map map = new HashMap();

        if (loginDtoNullCheck(loginDto)) {
            errorMapReturn(map);
        }

        Member member = null;
        try {
            member = new Member(loginDto);
            boolean idPwdValid = memberLoginService.idPwdValid(member);

            // idPwd 틀리면 오류폼
            if (!idPwdValid) {
                errorMapReturn(map);
            }
        } catch (UsernameNotFoundException e) {
            System.out.println("아이디가 없음");
            errorMapReturn(map);
            return map;
        }

        Map<String, String> tokens = memberLoginService.getTokens(member);
        String refreshToken = tokens.get("refreshToken");
        String accessToken = tokens.get("accessToken");

        // 각 토큰 저장
        memberLoginService.saveRefreshToken(member.getMemberId(), refreshToken);
        response.setHeader("Authorization", "Bearer "+accessToken);

        /**
         * 자동로그인기능 쿠키생성(나중에)
         */
        map.put("JWT", accessToken);
        System.out.println("error : "+ map.get("error"));
        return map;
    }

    private void errorMapReturn(Map map) {
        map.put("error", "error");
    }

    private boolean loginDtoNullCheck(LoginDto loginDto) {
        if (loginDto == null) {
            return true;
        }
        if (loginDto.getMemberId().equals("")) {
            return true;
        }
        if (loginDto.getPassword().equals("")) {
            return true;
        }
        if (loginDto.getMemberId().contains(" ")) {
            return true;
        }
        if (loginDto.getPassword().contains(" ")) {
            return true;
        }
        return false;
    }
}
