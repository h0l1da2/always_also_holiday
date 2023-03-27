package today.also.hyuil.service.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.*;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.util.Date;

@Transactional
@Service
public class MemberJoinServiceImpl implements MemberJoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberJoinServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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
