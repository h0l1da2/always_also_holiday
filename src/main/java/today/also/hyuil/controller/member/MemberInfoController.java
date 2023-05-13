package today.also.hyuil.controller.member;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.member.MemberInfoDto;
import today.also.hyuil.domain.dto.member.PwdDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.NotValidException;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/info")
public class MemberInfoController {

    private final WebService webService;
    private final MemberJoinService memberJoinService;

    public MemberInfoController(WebService webService, MemberJoinService memberJoinService) {
        this.webService = webService;
        this.memberJoinService = memberJoinService;
    }

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

    // TODO 패스워드 변경 구현
    @ResponseBody
    @PostMapping("/password")
    public ResponseEntity<String> modifyPwd(@RequestBody PwdDto pwdDto, HttpServletRequest request) {
        JsonObject jsonObject = new JsonObject();
        try {
            Long id = webService.getIdInSession(request);
            Member member = memberJoinService.findMyAccount(id);

            memberJoinService.passwordChange(id, pwdDto.getPassword(), pwdDto.getNewPwd());

        } catch (MemberNotFoundException e) {
            e.printStackTrace();
            return webService.badResponseEntity("MEMBER_NOT_FOUND");
        } catch (NotValidException e) {
            e.printStackTrace();
            return webService.badResponseEntity("NOT_VALID");
        }

        jsonObject.addProperty("data", "MODIFY_OK");
        return webService.okResponseEntity(jsonObject);
    }
}
