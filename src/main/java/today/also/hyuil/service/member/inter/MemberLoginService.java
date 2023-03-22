package today.also.hyuil.service.member.inter;

import today.also.hyuil.domain.member.Member;

import java.util.Map;

public interface MemberLoginService {

    boolean idPwdValid(Member member);
    Map<String, String> getTokens(Member member);
    void saveRefreshToken(String memberId, String refreshToken);
}
