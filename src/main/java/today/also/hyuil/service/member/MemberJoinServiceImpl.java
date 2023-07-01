package today.also.hyuil.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.NotValidException;
import today.also.hyuil.repository.member.MemberJpaRepository;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

// TODO 트랜잭션 삭제하고 필요할 때만 붙이기 (모든 서비스)
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberJoinServiceImpl implements MemberJoinService {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Member joinMember(Member member) {
        String encodedPassword = getEncodedPassword(member.getPassword());
        member.encodePassword(encodedPassword);
        return memberJpaRepository.save(member);
    }

    @Override
    public Member idDoubleCheck(String memberId) {
        return memberJpaRepository.findByMemberId(memberId).orElse(null);
    }

    @Override
    public Member nicknameCheck(String nickname) {
        return memberJpaRepository.findByNickname(nickname).orElse(null);
    }

    @Override
    public Member phoneCheck(String phone) {
        return memberJpaRepository.findByPhone(phone).orElse(null);
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
        Member findMember = memberJpaRepository.findByMemberId(memberId).orElseThrow(MemberNotFoundException::new);
        return passwordEncoder.matches(password, findMember.getPassword());
    }

    @Override
    public void passwordChange(Long id, String password, String newPwd) throws NotValidException, MemberNotFoundException {
        Member member = memberJpaRepository.findById(id).orElse(null);
        if (member == null) {
            throw new MemberNotFoundException();
        }

        boolean valid = passwordValid(password, member.getPassword());
        if (!valid) {
            throw new NotValidException("패스워드가 다릅니다");
        }
        member.passwordChange(newPwd);
        memberJpaRepository.save(member);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean passwordValid(String dbPwd, String clientPwd) {
        return passwordEncoder.matches(dbPwd, clientPwd);
    }

}
