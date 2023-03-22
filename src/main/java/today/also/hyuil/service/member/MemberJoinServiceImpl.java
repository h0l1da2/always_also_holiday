package today.also.hyuil.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

@Transactional
@Service
public class MemberJoinServiceImpl implements MemberJoinService {

    private final MemberRepository memberRepository;

    public MemberJoinServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member joinMember(Member member) {
        return memberRepository.insertMember(member);
    }

    @Override
    public Member idDoubleCheck(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    @Override
    public Member nicknameCheck(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    @Override
    public Member phoneCheck(String phone) {
        return memberRepository.findByPhone(phone);
    }




}
