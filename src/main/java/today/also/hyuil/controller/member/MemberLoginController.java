package today.also.hyuil.controller.member;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MemberLoginController {

    private final WebService webService;
    private final MemberJoinService memberJoinService;
    private final JwtTokenService jwtTokenService;

    public MemberLoginController(WebService webService, MemberJoinService memberJoinService, JwtTokenService jwtTokenService) {
        this.webService = webService;
        this.memberJoinService = memberJoinService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/loginForm")
    public String loginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("loginForm", new LoginDto());
        if (error != null && error.equals("error")) {
            model.addAttribute(error, "아이디 또는 비밀번호가 틀립니다");
        }
        return "member/loginForm";
    }

    @ResponseBody
    @PostMapping("/login")
    public Map loginToken(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        Map map = new HashMap();

        if (loginDtoNullCheck(loginDto)) {
            errorMapReturn(map);
        }

        try {
            boolean idPwdValid = memberJoinService.idPwdValid(loginDto.getMemberId(), loginDto.getPassword());
            // idPwd 틀리면 오류폼
            if (!idPwdValid) {
                errorMapReturn(map);
                return map;
            }
        } catch (UsernameNotFoundException e) {
            System.out.println("아이디가 없음");
            errorMapReturn(map);
            return map;
        }
        Member member = memberJoinService.findMyAccountMemberId(loginDto.getMemberId());
        Map<String, String> tokens = jwtTokenService.getTokens(
                member.getMemberId(), member.getRole().getName());
        String refreshToken = tokens.get("refreshToken");
        String accessToken = tokens.get("accessToken");

        // 각 토큰 저장
        jwtTokenService.saveRefreshToken(member.getMemberId(), refreshToken);

        /**
         * 자동로그인기능 쿠키생성(나중에)
         */
        map.put("JWT", accessToken);

        webService.sessionSetMember(member, request);

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
