package today.also.hyuil.service;

import org.springframework.stereotype.Service;
import today.also.hyuil.domain.dto.MemberJoinDto;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;

@Service
public class MemberJoinServiceImpl implements MemberJoinService {

    private final MemberRepository memberRepository;

    public MemberJoinServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member joinMember(Member member) {

        memberRepository.insertMember(member);

        return null;
    }


}
