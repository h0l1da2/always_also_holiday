package today.also.hyuil.member.controller;

import com.google.gson.JsonObject;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import today.also.hyuil.member.dto.MemberJoinDto;
import today.also.hyuil.member.domain.*;
import today.also.hyuil.member.domain.type.Mail;
import today.also.hyuil.member.domain.type.Name;
import today.also.hyuil.member.domain.type.Sns;
import today.also.hyuil.member.service.MailService;
import today.also.hyuil.member.service.MemberJoinService;
import today.also.hyuil.common.web.WebService;

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
            String resultId = jsonToString(memberId, "memberId");

            boolean nullCheck = webService.stringNullCheck(resultId);
            if (!nullCheck) {
                return webService.badResponseEntity("NULL");
            }
            if (!webService.validMemberId(memberId)) {
                return webService.badResponseEntity("BAD_FORM");
            }

            Member member = memberJoinService.idDoubleCheck(resultId);

            if (memberNullCheck(member)) {
                return webService.badResponseEntity("DUPL_ID");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }
        return okBodyResponse();
    }

    @ResponseBody
    @PostMapping("/nicknameCheck")
    public ResponseEntity<String> nicknameCheck(@RequestBody String nickname) {

        try {
            String resultNick = jsonToString(nickname, "nickname");

            if (!webService.stringNullCheck(resultNick)) {
                return webService.badResponseEntity("NULL");
            }

            if (!webService.validNickname(nickname)) {
                return webService.badResponseEntity("BAD_FORM");
            }

            Member member = memberJoinService.nicknameCheck(resultNick);
            if (memberNullCheck(member)) {
                return webService.badResponseEntity("DUPL_NICK");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }

        return okBodyResponse();
    }

    @ResponseBody
    @PostMapping("/phoneCheck")
    public ResponseEntity<String> phoneCheck(@RequestBody String phone) {
        try {
            String resultPhone = jsonToString(phone, "phone");

            if (!webService.stringNullCheck(phone)) {
                return webService.badResponseEntity("NULL");
            }

            if (!webService.validPhoneNumber(resultPhone)) {
                webService.badResponseEntity("FORM_FAIL");
            }

            Member member = memberJoinService.phoneCheck(resultPhone);
            if (memberNullCheck(member)) {
                return webService.badResponseEntity("DUPL_PHONE");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }
        return okBodyResponse();
    }

    @ResponseBody
    @PostMapping("/emailSend")
    public ResponseEntity<String> emailSend(@RequestBody String email) {
        try {

            if (!webService.stringNullCheck(email)) {
                return webService.badResponseEntity("NULL");
            }

            String resultEmail = jsonToString(email, "email");
            if (!webService.validEmail(resultEmail)) {
                return webService.badResponseEntity("FORM_FAIL");
            }

            randomCode = mailService.mailSend(Mail.JOIN, resultEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
            return webService.badResponseEntity("SEND_ERROR");
        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }

        return okBodyResponse();
    }

    @ResponseBody
    @PostMapping("/codeCheck")
    public ResponseEntity<String> codeCheck(@RequestBody String code) {
        try {
            String resultCode = jsonToString(code, "code");
            if (!webService.stringNullCheck(resultCode)) {
                return webService.badResponseEntity("NULL");
            }

            if (!resultCode.equals(randomCode)) {
                return webService.badResponseEntity("NOT_VALID");
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return webService.badResponseEntity("BAD_JSON");
        }
        return okBodyResponse();
    }

    @ResponseBody
    @PostMapping("/complete")
    public ResponseEntity<String> joinMember(@RequestBody @Valid MemberJoinDto memberJoinDto) {

        if (!webService.validMemberId(memberJoinDto.getMemberId())) {
            return webService.badResponseEntity("BAD_ID");
        }
        if (!webService.validNickname(memberJoinDto.getNickname())) {
            return webService.badResponseEntity("BAD_NICK");
        }
        if (!webService.validPwd(memberJoinDto.getPassword())) {
            return webService.badResponseEntity("BAD_PWD");
        }
        if (!webService.validEmail(memberJoinDto.getEmail())) {
            return webService.badResponseEntity("BAD_EMAIL");
        }
        if (!webService.validPhoneNumber(memberJoinDto.getPhone())) {
            return webService.badResponseEntity("BAD_PHONE");
        }

        memberJoinDto.setRoleName(Name.ROLE_USER);
        memberJoinDto.setSns(Sns.NONE);
        Member member =
                new Member(memberJoinDto,
                new Address(memberJoinDto),
                        new Role(memberJoinDto));
        memberJoinService.joinMember(member);
        return okBodyResponse();
    }

    @GetMapping("/complete")
    public String complete() {
        return "member/joinComplete";
    }

    private boolean memberNullCheck(Member member) {
        if (member != null) {
            return true;
        }
        return false;
    }

    private String jsonToString(String str, String key) throws ParseException {
        JSONObject jsonObject = webService.jsonParsing(str);
        String resultPhone = String.valueOf(jsonObject.get(key));
        return resultPhone;
    }

    private ResponseEntity<String> okBodyResponse() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data", "OK");

        return webService.okResponseEntity(jsonObject);
    }
}
