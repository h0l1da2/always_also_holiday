package today.also.hyuil.member.controller;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.member.dto.MemberInfoDto;
import today.also.hyuil.member.dto.PwdDto;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.MemberNotFoundException;
import today.also.hyuil.common.exception.NotValidException;
import today.also.hyuil.member.service.MemberJoinService;
import today.also.hyuil.common.web.WebService;

@Controller
@RequestMapping("/info")
@RequiredArgsConstructor
@Slf4j
public class MemberInfoController {

    private final WebService webService;
    private final MemberJoinService memberJoinService;

    @GetMapping
    public String info() {
        return "member/info/infoMain";
    }

    @GetMapping("/modify")
    public String infoModify(HttpServletRequest request, Model model) {
        try {
            Long id = webService.getIdInSession(request);

            Member member = memberJoinService.findMyAccount(id);
            model.addAttribute("member", new MemberInfoDto(member));

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return "redirect:/loginForm?redirectUrl=/info";
        }

        return "member/info/infoForm";
    }

    @GetMapping("/password")
    public String infoPwd() {
        return "member/info/passwordForm";
    }

    @ResponseBody
    @PutMapping("/password")
    public ResponseEntity<String> modifyPwd(@RequestBody @Valid PwdDto pwdDto, HttpServletRequest request) {
        try {
            Long id = webService.getIdInSession(request);
//            Long id = 1L;

            if (!webService.validPwd(pwdDto.getNewPwd())) {
                return webService.badResponseEntity("BAD_FORM");
            }

            memberJoinService.passwordChange(id, pwdDto.getPassword(), pwdDto.getNewPwd());

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (NotValidException e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_VALID");
        }

        return okResponseEntity();
    }



    private ResponseEntity<String> okResponseEntity() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", "OK");
        return webService.okResponseEntity(jsonObject);
    }
}
