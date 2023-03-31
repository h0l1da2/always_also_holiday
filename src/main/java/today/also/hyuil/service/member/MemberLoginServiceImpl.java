package today.also.hyuil.service.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberLoginService;

@Transactional
@Service
public class MemberLoginServiceImpl implements MemberLoginService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberLoginServiceImpl(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean idPwdValid(Member member) {
        Member findMember = getMember(member);
        return passwordEncoder.matches(member.getPassword(), findMember.getPassword());
    }

    private Member getMember(Member member) {
        return memberRepository.findByMemberIdRole(member.getMemberId());
    }


}

