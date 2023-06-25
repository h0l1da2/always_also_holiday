package today.also.hyuil.controller.member;

import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
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

@Controller
@RequestMapping("/join")
@RequiredArgsConstructor
@Slf4j
public class MemberJoinController {

    private String randomCode;
    private final MemberJoinService memberJoinService;
    private final MailService mailService;
    private final WebService webService;

    @GetMapping
    public String joinForm(Model model) {
        model.addAttribute("member", new MemberJoinDto());
        return "member/joinForm";
    }

    @ResponseBody
    @PostMapping("/idCheck")
    public ResponseEntity<String> idDoubleCheck(@RequestBody String memberId) {
        try {
            // memberId 제이슨 풀기
            JSONObject jsonObject = webService.jsonParsing(memberId);
            String resultId = String.valueOf(jsonObject.get("memberId"));

            boolean nullCheck = webService.stringNullCheck(resultId);
            if (!nullCheck) {
                return webService.badResponseEntity("NULL");
            }

            Member member = memberJoinService.idDoubleCheck(resultId);

            if (memberNullCheck(member)) {
                return webService.badResponseEntity("DUPL_ID");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("BODY", "OK");

        return webService.okResponseEntity(jsonObject);
    }

    @ResponseBody
    @PostMapping("/nicknameCheck")
    public ResponseEntity<String> nicknameCheck(@RequestBody String nickname) {

        try {
            JSONObject jsonObject = webService.jsonParsing(nickname);

            String resultNick = String.valueOf(jsonObject.get("nickname"));

            if (!webService.stringNullCheck(resultNick)) {
                return webService.badResponseEntity("NULL");
            }

            Member member = memberJoinService.nicknameCheck(resultNick);
            if (memberNullCheck(member)) {
                return webService.badResponseEntity("DUPL_NICK");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("BODY", "OK");

        return webService.okResponseEntity(jsonObject);
    }

    // TODO 폰 양식 체크도 서버에서 한번 더
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

    // TODO 이메일 양식 체크도 서버에서 한번 더
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
