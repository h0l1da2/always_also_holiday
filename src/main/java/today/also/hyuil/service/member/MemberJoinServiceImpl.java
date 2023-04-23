package today.also.hyuil.service.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

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
        String encodedPassword = getEncodedPassword(member.getPassword());
        member.encodePassword(encodedPassword);
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

    @Override
    public Member findMyAccount(String memberId) {
        return memberRepository.findByMemberIdRole(memberId);
    }

    @Override
    public boolean idPwdValid(String memberId, String password) {
        Member findMember = memberRepository.findByMemberId(memberId);
        return passwordEncoder.matches(password, findMember.getPassword());
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
