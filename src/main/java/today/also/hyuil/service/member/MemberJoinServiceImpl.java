package today.also.hyuil.service.member;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.NotValidException;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

// TODO 트랜잭션 삭제하고 필요할 때만 붙이기 (모든 서비스)
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
    public Member findMyAccountMemberId(String memberId) {
        return memberRepository.findByMemberIdRole(memberId);
    }

    @Override
    public Member findMyAccount(Long id) {
        return memberRepository.findByIdRole(id);
    }

    @Override
    public boolean idPwdValid(String memberId, String password) throws MemberNotFoundException {
        Member findMember = memberRepository.findByMemberId(memberId);
        if (findMember == null) {
            throw new MemberNotFoundException();
        }
        return passwordEncoder.matches(password, findMember.getPassword());
    }

    @Override
    public void passwordChange(Long id, String password, String newPwd) throws NotValidException {
        Member member = memberRepository.findById(id);

        boolean valid = passwordValid(newPwd, member.getPassword());
        if (valid) {
            throw new NotValidException("패스워드가 다릅니다");
        }

        memberRepository.updatePassword(id, newPwd);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean passwordValid(String dbPwd, String clientPwd) {
        return passwordEncoder.matches(dbPwd, clientPwd);
    }

}
