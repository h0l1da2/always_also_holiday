package today.also.hyuil.controller.member;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.domain.dto.member.DoubleCheckDto;
import today.also.hyuil.domain.dto.member.MemberJoinDto;
import today.also.hyuil.domain.member.*;
import today.also.hyuil.service.member.inter.MailService;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import jakarta.mail.MessagingException;

@Controller
@RequestMapping("/join")
public class MemberJoinController {

    private String randomCode;
    private final MemberJoinService memberJoinService;
    private final MailService mailService;
    private final WebService webService;

    public MemberJoinController(MemberJoinService memberJoinService, MailService mailService, WebService webService) {
        this.memberJoinService = memberJoinService;
        this.mailService = mailService;
        this.webService = webService;
    }

    @GetMapping
    public String joinForm(Model model) {
        model.addAttribute("member", new MemberJoinDto());
        return "member/joinForm";
    }

    @ResponseBody
    @PostMapping("/idCheck")
    public String idDoubleCheck(@RequestBody DoubleCheckDto doubleCheckDto) {
        if (stringNullCheck(
                doubleCheckDto.getMemberId())) {
            return "확인 불가";
        }

        Member member = memberJoinService.idDoubleCheck(
                doubleCheckDto.getMemberId()
        );

        if (memberNullCheck(member)) {
            return "중복";
        }
        return "가입 가능";
    }

    @ResponseBody
    @PostMapping("/nicknameCheck")
    public String nicknameCheck(@RequestBody DoubleCheckDto doubleCheckDto) {
        if (stringNullCheck(
                doubleCheckDto.getNickname())) {
            return "확인 불가";
        }

        Member member = memberJoinService.nicknameCheck(
                doubleCheckDto.getNickname()
        );
        if (memberNullCheck(member)) {
            return "중복";
        }
        return "가입 가능";
    }

    @ResponseBody
    @PostMapping("/phoneCheck")
    public String phoneCheck(@RequestBody DoubleCheckDto doubleCheckDto) {
        if (stringNullCheck(
                doubleCheckDto.getPhone())) {
            return "확인 불가";
        }

        Member member = memberJoinService.phoneCheck(
                doubleCheckDto.getPhone()
        );
        if (memberNullCheck(member)) {
            return "중복";
        }
        return "가입 가능";
    }

    @ResponseBody
    @PostMapping("/emailSend")
    public ResponseEntity<String> emailSend(@RequestParam String email) {
        JsonObject jsonObject = new JsonObject();
        try {
            randomCode = mailService.mailSend(Mail.JOIN, email);
        } catch (MessagingException e) {
            e.printStackTrace();
            jsonObject.addProperty("error", "SEND_ERROR");
            webService.badResponseEntity("SEND_ERROR");
        }
        jsonObject.addProperty("body", "SEND_OK");
        return webService.okResponse(jsonObject);
    }

    @ResponseBody
    @PostMapping("/codeCheck")
    public String codeCheck(@RequestBody DoubleCheckDto doubleCheckDto) {
        String code = doubleCheckDto.getCode();
        if (stringNullCheck(code)) {
            return "확인 불가";
        }

        if (code.equals(randomCode)) {
            return "코드 일치";
        }
        return "코드 불일치";
    }

    @PostMapping("/complete")
    public String joinMember(@ModelAttribute MemberJoinDto memberJoinDto) {
        memberJoinDto.setRoleName(Name.ROLE_USER);
        Member member =
                new Member(memberJoinDto,
                new Address(memberJoinDto),
                        new Role(memberJoinDto));
        memberJoinService.joinMember(member);
        return "member/joinComplete";
    }

    private boolean stringNullCheck(String str) {
        if (str == null) {
            return true;
        }
        if (str.equals("")) {
            return true;
        }
        if (str.contains(" ")) {
            return true;
        }
        return false;
    }

    private boolean memberNullCheck(Member member) {
        if (member != null) {
            return true;
        }
        return false;
    }

}
