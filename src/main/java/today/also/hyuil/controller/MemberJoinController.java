package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.dto.member.MemberJoinDto;
import today.also.hyuil.domain.member.Address;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Role;
import today.also.hyuil.service.member.MemberJoinService;

@Controller
public class MemberJoinController {

    private final MemberJoinService memberJoinService;

    public MemberJoinController(MemberJoinService memberJoinService) {
        this.memberJoinService = memberJoinService;
    }

    @ResponseBody
    @PostMapping
    public String idDoubleCheck(@RequestBody LoginDto loginDto) {
        String memberId = loginDto.getMemberId();
        if (memberId == null) {
            return "확인 불가";
        }

        Member member = memberJoinService.idDoubleCheck(memberId);
        if (member != null) {
            return "중복";
        }
        return "가입 가능";
    }

    @PostMapping
    public String joinMember(MemberJoinDto memberJoinDto) {

        Member member = new Member(memberJoinDto,
                new Address(memberJoinDto), new Role(memberJoinDto));
        memberJoinService.joinMember(member);
        return "member/joinComplete";
    }
}
