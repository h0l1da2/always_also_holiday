package today.also.hyuil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import today.also.hyuil.domain.dto.MemberJoinDto;
import today.also.hyuil.domain.member.Address;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Role;
import today.also.hyuil.service.MemberJoinService;

@Controller
public class MemberJoinController {

    private final MemberJoinService memberJoinService;

    public MemberJoinController(MemberJoinService memberJoinService) {
        this.memberJoinService = memberJoinService;
    }

    @PostMapping
    public String joinMember(MemberJoinDto memberJoinDto) {

        Member member = new Member(memberJoinDto,
                new Address(memberJoinDto), new Role(memberJoinDto));
        memberJoinService.joinMember(member);

        return "완료";
    }
}
