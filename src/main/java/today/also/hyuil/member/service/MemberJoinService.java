package today.also.hyuil.member.service;

import today.also.hyuil.member.domain.Member;
import today.also.hyuil.common.exception.MemberNotFoundException;
import today.also.hyuil.common.exception.NotValidException;

public interface MemberJoinService {
    Member joinMember(Member member);
    Member idDoubleCheck(String memberId);
    Member nicknameCheck(String nickname);
    Member phoneCheck(String phone);
    Member findMyAccountMemberId(String memberId);
    Member findMyAccount(Long id);
    boolean idPwdValid(String memberId, String password) throws MemberNotFoundException;
    void passwordChange(Long id, String password, String newPwd) throws NotValidException, MemberNotFoundException;
}
