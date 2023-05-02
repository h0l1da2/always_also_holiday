package today.also.hyuil.service.member.inter;

import today.also.hyuil.domain.member.Member;

public interface MemberJoinService {
    Member joinMember(Member member);
    Member idDoubleCheck(String memberId);
    Member nicknameCheck(String nickname);
    Member phoneCheck(String phone);
    Member findMyAccountMemberId(String memberId);
    Member findMyAccount(Long id);
    boolean idPwdValid(String memberId, String password);

}
