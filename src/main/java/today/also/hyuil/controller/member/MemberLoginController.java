package today.also.hyuil.controller.member;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberLoginController {

    private final WebService webService;
    private final MemberJoinService memberJoinService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/loginForm")
    public String loginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("loginForm", new LoginDto());
        if (error != null && error.equals("error")) {
            model.addAttribute(error, "아이디 또는 비밀번호가 틀립니다");
        }
        return "member/loginForm";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "/index";
       }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<String> loginToken(@RequestBody @Valid LoginDto loginDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();

        try {
            boolean idPwdValid = memberJoinService.idPwdValid(loginDto.getMemberId(), loginDto.getPassword());

            if (!idPwdValid) {
                return webService.badResponseEntity("NOT_VALID");
            }

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        }
        Member member = memberJoinService.findMyAccountMemberId(loginDto.getMemberId());
        Map<String, String> tokens = jwtTokenService.getTokens(
                member.getMemberId(), member.getRole().getName());
        String refreshToken = tokens.get("refreshToken");
        String accessToken = tokens.get("accessToken");

        // 각 토큰 저장
        jwtTokenService.saveRefreshToken(member.getMemberId(), refreshToken);

        // TODO 자동로그인 기능 쿠키 생성 나중에 추가
        jsonObject.addProperty("JWT", accessToken);

        webService.sessionSetMember(member, request);

        return webService.okResponseEntity(jsonObject);
    }
    
}
